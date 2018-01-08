package com.tapc.platform.model.scancode;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tapc.platform.library.controller.MachineController;
import com.tapc.platform.model.tcp.SocketListener;
import com.tapc.platform.model.tcp.TcpClient;
import com.tapc.platform.utils.PreferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ScanQrcodeModel {
	public static final String IP = "112.74.87.166";
	public static final int PORT = 1234;
	private Context mContext;
	private String mDeviceId = "";
	private boolean isUploadDeviceInfor = false;
	private TcpClient mTcpClient;
	private String mQrcodeStr = "";
	private String mLoginPassword;
	public static boolean mNeedChangeQrcode = true;
	private boolean mCurrentConnectStatus = false;
	private TcpListener mTcpListener;

	public interface TcpListener {
		void showQrcode(String qrcodeStr);

		void connectServerResult(boolean isSuccess);

		void openDevice(User user);

		User getUser();

		void recvSportPlan(int userId, List<SportData> plan_load);
	}

	public void setTcpListener(@NonNull TcpListener tcpListener) {
		mTcpListener = tcpListener;
	}

	public void init(Context context) {
		mContext = context;
	}

	public String getDeviceId() {
		String deviceId = "";
		WifiManager mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		if (mWifiManager.isWifiEnabled()) {
			deviceId = SysUtils.getLocalMacAddress(mContext);
		} else {
			mWifiManager.setWifiEnabled(true);
			deviceId = SysUtils.getLocalMacAddress(mContext);
			mWifiManager.setWifiEnabled(false);
		}
		return deviceId;
	}

	public void connectServer() {
		new Thread(mRunnable).start();
		new Thread(mQrRunnable).start();
	}

	private void connectServerResult(boolean isSuccess) {
		if (isSuccess == false) {
			stopTcpClient();
		}
		if (mTcpListener != null) {
			mTcpListener.connectServerResult(isSuccess);
		}
	}

	private boolean connect() {
		boolean isConnected = false;
		try {
			if (mTcpListener != null) {
				mTcpListener.showQrcode(null);
			}
			Config.isConnected = SysUtils.isNetConnected(mContext);
			if (Config.isConnected) {
				if (TextUtils.isEmpty(mDeviceId)) {
					mDeviceId = PreferenceHelper.readString(mContext, Config.SETTING_CONFIG, "device_id", null);
					if (TextUtils.isEmpty(mDeviceId)) {
						mDeviceId = getDeviceId();
						PreferenceHelper.write(mContext, Config.SETTING_CONFIG, "device_id", mDeviceId);
					}
				}
				if (!TextUtils.isEmpty(mDeviceId)) {
					Config.DEVICE_ID = mDeviceId;
					Log.d("tcp connect", "start  device id : " + mDeviceId);
					String ip = GetInetAddress(UrlConsts.SH_SOCKET);
					if (TextUtils.isEmpty(ip)) {
						ip = IP;
					}
					mTcpClient = new TcpClient();
					mTcpClient.setListener(mSoketListener);
					isConnected = mTcpClient.connect(ip, PORT, 4000);
				} else {
					isConnected = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isConnected;
	}

	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			while (true) {
				synchronized (this) {
					if (mCurrentConnectStatus == false) {
						stopTcpClient();
						mCurrentConnectStatus = connect();
						connectServerResult(mCurrentConnectStatus);
					}
					SystemClock.sleep(3000);
				}
			}
		}
	};

	public String GetInetAddress(String host) {
		String IPAddress = "";
		InetAddress ReturnStr1 = null;
		try {
			ReturnStr1 = InetAddress.getByName(host);
			IPAddress = ReturnStr1.getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return IPAddress;
	}

	private void stopTcpClient() {
		cancelHearbeatTimer();
		if (mTcpClient != null) {
			mTcpClient.stop();
			mTcpClient = null;
		}
	}

	/**
	 * 服务器连接的心跳包
	 */
	private boolean isHeartbeatTimeout;
	private Timer mHeartbeatTimer;
	private int mNotHeartbeatCount;
	private int mNotHeartbeatNumber;

	private void cancelHearbeatTimer() {
		mNotHeartbeatCount = 0;
		isHeartbeatTimeout = false;
		if (mHeartbeatTimer != null) {
			mHeartbeatTimer.cancel();
			mHeartbeatTimer = null;
		}
	}

	private void initHeartbeat() {
		cancelHearbeatTimer();
		mHeartbeatTimer = new Timer();
		mHeartbeatTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				isHeartbeatTimeout = true;
				sendHeartbeat(mTcpClient, Command.HEARTBEAT, mDeviceId);
				SystemClock.sleep(3000);
				if (isHeartbeatTimeout) {
					mNotHeartbeatCount++;
					if (mNotHeartbeatCount >= 2) {
						cancelHearbeatTimer();
						mCurrentConnectStatus = false;
						mNotHeartbeatNumber++;
						Log.d("recv hearbeat", "failed number : " + mNotHeartbeatNumber);
					}
				} else {
					mNotHeartbeatCount = 0;
				}
			}
		}, 3000, 10000);
	}

	private SocketListener mSoketListener = new SocketListener() {

		@Override
		public void onOpen(boolean isConnect) {
			Log.d("tcp connect", "" + isConnect);
			if (isConnect) {
				initHeartbeat();
			}
		}

		@Override
		public void onMessage(byte[] dataBuffer) {
			try {
				String jsonStr = new String(dataBuffer);
				JSONObject jsonObject = new JSONObject(jsonStr);
				int command = (int) jsonObject.get("command");
				switch (command) {
				case Command.HEARTBEAT:
					isHeartbeatTimeout = false;
					break;
				case Command.OPEN_DEVICE:
					OpenDeviceRequest openDeviceRequest = (OpenDeviceRequest) getGsonToObject(jsonStr,
							OpenDeviceRequest.class);
					if (openDeviceRequest != null) {
						if (openDeviceRequest.getDevice_id().endsWith(mDeviceId) && openDeviceRequest.getUser_id() > 0) {
							sendOpenDeviceStatus(mTcpClient, Command.OPEN_DEVICE, mDeviceId,
									openDeviceRequest.getUser_id(), "0");
							User user = new User();
							user.setName(openDeviceRequest.getUser_name());
							user.setUserId(openDeviceRequest.getUser_id());
							user.setDeviceId(openDeviceRequest.getDevice_id());
							user.setAvatarUrl(openDeviceRequest.getUser_avatar());
							if (mTcpListener != null) {
								mTcpListener.openDevice(user);
								mNeedChangeQrcode = true;
							}
							return;
						}
					}
					sendOpenDeviceStatus(mTcpClient, Command.OPEN_DEVICE, mDeviceId, openDeviceRequest.getUser_id(),
							"1");
					break;
				case Command.SPORTS_PLAN:
					SportsMenu sportsMenu = (SportsMenu) getGsonToObject(jsonStr, SportsMenu.class);
					if (sportsMenu != null && sportsMenu.getDevice_id().equals(mDeviceId)
							&& sportsMenu.getAction_count() > 0) {
						int actionCount = sportsMenu.getAction_count();
						List<SportData> plan_load = new ArrayList<SportData>();
						if (actionCount > 0) {
							for (int i = 0; i < actionCount; i++) {
								String key = "plan_load" + (i + 1);
								if (jsonObject.has(key)) {
									String sportDataStr = jsonObject.get(key).toString();
									if (sportDataStr != null && !sportDataStr.isEmpty()) {
										SportData sportData = new Gson().fromJson(sportDataStr, SportData.class);
										plan_load.add(sportData);
									}
								}
							}
							mTcpListener.recvSportPlan(sportsMenu.getUser_id(), plan_load);
						}
					}
					break;
				default:
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onError(String error) {
		}

		@Override
		public void onClose() {
		}
	};

	/**
	 * 获取二维码
	 */
	private interface HttpListener {
		void finsh();
	}

	private int mRequestResult = 0;
	private Runnable mQrRunnable = new Runnable() {
		@Override
		public void run() {
			while (true) {
				if (mCurrentConnectStatus && isScanQrShow) {
					synchronized (this) {

						mRequestResult = 1;
						startShowQrcode(new HttpListener() {

							@Override
							public void finsh() {
								mRequestResult = 0;
							}
						});

						while (mRequestResult != 0) {
							if (mRequestResult > 0) {
								mRequestResult++;
							}
							if (mRequestResult >= 16) {
								break;
							}
							SystemClock.sleep(1000);
						}

						mRequestResult = 1;
						initRandomcode(new HttpListener() {

							@Override
							public void finsh() {
								mRequestResult = 0;
							}

						});

						while (mRequestResult != 0) {
							if (mRequestResult > 0) {
								mRequestResult++;
							}
							if (mRequestResult >= 5) {
								break;
							}
							SystemClock.sleep(1000);
						}
					}
					SystemClock.sleep(2000);
				} else {
					SystemClock.sleep(500);
				}
			}
		}
	};

	private void startShowQrcode(HttpListener listener) {
		if (checkUploadDeviceInforStatus()) {
			if (mNeedChangeQrcode) {
				getQrcodeOrRandomcode(mDeviceId, GetLoginType.QRCODE, listener);
			} else {
				if (mTcpListener != null && mTcpClient != null && mTcpClient.isConnecting()) {
					mTcpListener.showQrcode(mQrcodeStr);
				}
				listener.finsh();
			}
		} else {
			uploadDeviceInformation(listener);
		}
	}

	private boolean isScanQrShow = false;

	public void setScanQrShowStatus(boolean isShow) {
		isScanQrShow = isShow;
	}

	private class GetLoginType {
		private static final String QRCODE = "qrcode";
		private static final String RANDOMCODE = "randomcode";
		private static final String ADVERTISMENT = "advertisement";
	}

	private void getQrcodeOrRandomcode(String deviceId, final String type, final HttpListener listener) {
		TapcApp.getInstance().getHttpClient().getQrOrRandomcode(deviceId, type, new Callback() {

			@Override
			public void onSuccess(String result) {

			}

			@Override
			public void onSuccess(Object o) {
				if (o != null) {
					switch (type) {
					case GetLoginType.QRCODE:
						GetQrcodeResponse getQrcodeResponse = (GetQrcodeResponse) o;
						if (getQrcodeResponse.getStatus() == 0) {
							Qrcode qrcode = getQrcodeResponse.getResponse();
							if (qrcode != null) {
								mQrcodeStr = qrcode.getQrcode_url();
								mNeedChangeQrcode = false;
							}
						} else {
							mQrcodeStr = "" + mDeviceId;
							mNeedChangeQrcode = false;
						}
						if (mTcpListener != null && !TextUtils.isEmpty(mQrcodeStr)) {
							mTcpListener.showQrcode(mQrcodeStr);
							Log.d("get qrcode result",
									"" + getQrcodeResponse.getStatus() + "  " + getQrcodeResponse.getMessage()
											+ " url : " + mQrcodeStr);
						}
						listener.finsh();
						break;
					case GetLoginType.RANDOMCODE:
						GetRandomcodeResponse getRandomcodeResponse = (GetRandomcodeResponse) o;
						if (getRandomcodeResponse.getStatus() == 0) {
							Randomcode randomcode = getRandomcodeResponse.getResponse();
							if (randomcode != null) {
								String password = randomcode.getRandom_code();
								if (password != null && !password.isEmpty()) {
									PreferenceHelper.write(mContext, Config.SETTING_CONFIG, "randomcode",
											new Gson().toJson(randomcode));
									mLoginPassword = password;
									Log.d("login password", "" + password);
								}
							}
						}
						listener.finsh();
						break;
					case GetLoginType.ADVERTISMENT:
						listener.finsh();
						break;
					default:
						break;
					}
				}
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onFailure(Object o) {
				listener.finsh();
				Log.d("getQrcodeOrRandomcode", "failed");
			}
		});
	}

	/**
	 * 初始化登录的随机码
	 * 
	 */
	private void initRandomcode(HttpListener listener) {
		if (mLoginPassword != null) {
			listener.finsh();
			return;
		}
		String randomcodeStr = PreferenceHelper.readString(mContext, Config.SETTING_CONFIG, "randomcode");
		if (randomcodeStr != null) {
			Randomcode randomcode = new Gson().fromJson(randomcodeStr, Randomcode.class);
			String recodeDate = randomcode.getDeadtime();
			long nowTime = System.currentTimeMillis();
			String nowDate = SysUtils.getDataTimeStr("yyyy-MM-dd", nowTime);
			String password = randomcode.getRandom_code();
			if (!nowDate.equals(recodeDate) || password == null || (password != null && password.isEmpty())) {
				getQrcodeOrRandomcode(mDeviceId, GetLoginType.RANDOMCODE, listener);
			} else {
				mLoginPassword = password;
				listener.finsh();
			}
		} else {
			getQrcodeOrRandomcode(mDeviceId, GetLoginType.RANDOMCODE, listener);
		}
	}

	public String getLoginPassword() {
		return mLoginPassword;
	}

	/**
	 * 上传设备信息
	 * 
	 */
	private boolean checkUploadDeviceInforStatus() {
		if (isUploadDeviceInfor) {
			return true;
		}
		isUploadDeviceInfor = PreferenceHelper.readBoolean(mContext, Config.SETTING_CONFIG, "is_upload_device_infor",
				false);
		return isUploadDeviceInfor;
	}

	private void uploadDeviceInformation(final HttpListener listener) {
		DeviceInformationParamter parameter = new DeviceInformationParamter();
		parameter.setTime("" + (60 * 120));
		parameter.setSpeed("" + TapcApp.MAX_SPEED);
		parameter.setIncline("" + TapcApp.MAX_INCLINE);
		String parameterStr = new Gson().toJson(parameter).toString();

		DeviceInfor deviceInfor = getDeviceInfor();
		TapcApp.getInstance()
				.getHttpClient()
				.uploadDevicInformation(mDeviceId, deviceInfor.getType(), deviceInfor.getModel(), parameterStr,
						new Callback() {

							@Override
							public void onSuccess(String result) {

							}

							@Override
							public void onSuccess(Object o) {
								ResponseDTO<String> responseDTO = (ResponseDTO<String>) o;
								if (responseDTO != null
										&& (responseDTO.getStatus() == 0 || responseDTO.getStatus() == 5)) {
									Log.d("uploadDevicInformation", "successful");
									PreferenceHelper.write(mContext, Config.SETTING_CONFIG, "is_upload_device_infor",
											true);
									isUploadDeviceInfor = true;
									getQrcodeOrRandomcode(mDeviceId, GetLoginType.QRCODE, listener);
								} else {
									listener.finsh();
								}
							}

							@Override
							public void onStart() {

							}

							@Override
							public void onFailure(Object o) {
								listener.finsh();
								Log.d("uploadDeviceInformation", "failed");
							}
						});
	}

	private DeviceInfor getDeviceInfor() {
		MachineController controller = MachineController.getInstance();
		controller.sendCtlVersionCmd();
		SystemClock.sleep(1000);
		String mcuVersion = "v" + controller.getCtlVersionValue();
		DeviceInfor[] inforList = DeviceInfor.values();
		DeviceInfor deviceInfor = inforList[0];
		for (DeviceInfor infor : inforList) {
			if (mcuVersion.startsWith(infor.getMcuVersion())) {
				deviceInfor = infor;
				break;
			}
		}
		return deviceInfor;
	}

	public class Command {
		public static final int HEARTBEAT = 1;
		public static final int OPEN_DEVICE = 2;
		public static final int SPORTS_PLAN = 3;
	};

	public <T> Object getGsonToObject(String jsonStr, Class<T> cls) {
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, cls);
	}

	private String getHeartbeatJson(int command, String deviceId) {
		HeartbeatPacket heartbeatPacket = new HeartbeatPacket(command, deviceId);
		Gson gson = new Gson();
		String str = gson.toJson(heartbeatPacket).toString();
		return str;
	}

	public void sendHeartbeat(TcpClient tcpClient, int command, String deviceId) {
		String jsonStr = getHeartbeatJson(command, deviceId);
		if (jsonStr != null && tcpClient != null) {
			tcpClient.sendData(jsonStr);
		}
	}

	private String getOpenDeviceAckJson(int command, String device_id, int user_id, String status) {
		OpenDeviceAck openDeviceAck = new OpenDeviceAck(command, device_id, user_id, status);
		Gson gson = new Gson();
		String str = gson.toJson(openDeviceAck).toString();
		return str;
	}

	public void sendOpenDeviceStatus(TcpClient tcpClient, int command, String device_id, int user_id, String status) {
		String jsonStr = getOpenDeviceAckJson(command, device_id, user_id, status);
		if (jsonStr != null && tcpClient != null) {
			tcpClient.sendData(jsonStr);
		}
	}
}
