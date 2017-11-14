package com.tapc.platform.model.rfid;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;


public class RfidCtl {
    private RfidAdapter mAdapter;
    private static final int ACK_SUCCESS = 0;
    private static final int ACK_FAIL = 1;
    private byte[] mCardUid;
    private byte mNowCmdCode = 0;
    private RfidListener mRfidListener;
    private boolean isConnectDevice = false;

    public RfidCtl() {
    }

    /**
     * 功能描述 : 配置与RFID串口通信适配器
     */
    public void setAdapter(@NonNull RfidAdapter adapter) {
        mAdapter = adapter;
        mAdapter.setRecvListerner(mRecvListener);
    }

    /**
     * 功能描述 : 初始化通讯波特率
     */
    public void init() {
        byte[] datas = new byte[1];
        mAdapter.sendData(datas.length, datas);
        SystemClock.sleep(5);
        mAdapter.sendData(datas.length, datas);
    }

    /**
     * 功能描述 : 获取版本号
     */
    public void getDvcInfo() {
        setConnectDevice(false);
        sendCmd(RfidCmdClass.DEVICE_CTL, RfidCtlCmdCode.GET_DVC_INFO, null);
    }

    public void PCDSetTX(byte ucSelTX) {
        byte[] infor = new byte[]{ucSelTX};
        sendCmd(RfidCmdClass.DEVICE_CTL, RfidCardCmdCode.PCD_SET_TX, infor);
    }

    public void activationCard() {
        byte[] infor = new byte[]{(byte) 0x00, (byte) 0x26};
        sendCmd(RfidCmdClass.MIFARE_S50_S70, RfidCardCmdCode.MF_ACTIVATE, infor);
    }

    public void authent(byte[] uid, int block) {
        if (uid != null) {
            byte[] passward = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] infor = new byte[12];
            infor[0] = 0x60;
            System.arraycopy(uid, 0, infor, 1, uid.length);
            System.arraycopy(passward, 0, infor, 5, passward.length);
            infor[infor.length - 1] = (byte) (block & 0xff);
            sendCmd(RfidCmdClass.MIFARE_S50_S70, RfidCardCmdCode.MF_AUTHENT, infor);
        }
    }

    /**
     * 功能描述 : 设置自动检测卡模式
     */
    public boolean piceAutoDetect(byte ADMode, byte txMode, byte reqCode, byte authMode, byte keyType, byte[] key,
                                  int block) {
        int inforLength = 0;
        if (authMode == 'F') {
            inforLength = 12;
        } else {
            return false;
        }
        byte[] infor = new byte[inforLength];
        infor[0] = ADMode;
        infor[1] = txMode;
        infor[2] = reqCode;
        infor[3] = authMode;
        infor[4] = keyType;
        byte[] passward = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        System.arraycopy(passward, 0, infor, 5, passward.length);
        infor[inforLength - 1] = (byte) (block & 0xff);
        sendCmd(RfidCmdClass.MIFARE_S50_S70, RfidCardCmdCode.PICE_AUTO_DETECT, infor);
        return true;
    }

    /**
     * 功能描述 : 读卡数据
     *
     * @param : block 块区
     */
    public void mfGetValue(int block) {
        byte[] infor = new byte[]{(byte) (block & 0xff)};
        sendCmd(RfidCmdClass.MIFARE_S50_S70, RfidCardCmdCode.MF_READ, infor);
    }

    /**
     * 功能描述 : 写卡数据
     *
     * @param : block 块区
     * @param : value  数据
     */
    public boolean mfSetValue(int block, byte[] datas) {
        if (block == 1 || block % 4 == 3 || datas == null) {
            return false;
        }

        int length = datas.length;
        if (length > 16) {
            return false;
        }
        byte[] infor = new byte[length + 1];
        infor[0] = (byte) block;
        System.arraycopy(datas, 0, infor, 1, length);
        sendCmd(RfidCmdClass.MIFARE_S50_S70, RfidCardCmdCode.MF_WRITE, infor);
        return true;
    }

    public boolean mfSetAuthent(int block, byte[] passward) {
        if (block % 4 != 3) {
            return false;
        }
        if (passward == null) {
            return false;
        } else if (passward.length != 16) {
            return false;
        }
        int length = passward.length;
        byte[] infor = new byte[length + 1];
        infor[0] = (byte) block;
        System.arraycopy(passward, 0, infor, 1, length);
        sendCmd(RfidCmdClass.MIFARE_S50_S70, RfidCardCmdCode.MF_WRITE, infor);
        return true;
    }

    public int checkCmdStatus(byte[] data) {
        if (data[2] == ACK_SUCCESS) {
            return ACK_SUCCESS;
        } else {
            return ACK_FAIL;
        }
    }

    /**
     * 功能描述 : 数据接收监听，总处理
     */
    private RecvListener mRecvListener = new RecvListener() {

        @Override
        public void update(Object data) {
            byte[] datas = (byte[]) data;
            int length = datas.length;
            byte cmdClass = datas[1];
            if (checkCmdStatus(datas) == ACK_SUCCESS) {
                Log.d("rfid ack", "success");
            } else {
                Log.d("rfid ack", "failed");
                return;
            }
            switch (cmdClass) {
                case RfidCmdClass.DEVICE_CTL:
                    deviceCtlCmd(datas);
                    break;
                case RfidCmdClass.MIFARE_S50_S70:
                    mifareCmd(datas);
                    break;
                default:
                    break;
            }
            mNowCmdCode = -1;
        }
    };

    /**
     * 功能描述 : 设备控制命令处理
     */
    private void deviceCtlCmd(byte[] datas) {
        byte cmd = mNowCmdCode;
        switch (cmd) {
            case RfidCtlCmdCode.GET_DVC_INFO:
                if (datas != null) {
                    byte[] infoBytes = new byte[(datas[3] & 0xff)];
                    System.arraycopy(datas, 4, infoBytes, 0, infoBytes.length);
                    String version = new String(infoBytes);
                    if (version != null && !version.isEmpty()) {
                        setConnectDevice(true);
                        Log.d("rfid version", version);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 功能描述 : S50/S70 卡类处理命令
     */
    private void mifareCmd(byte[] datas) {
        byte cmd = mNowCmdCode;
        switch (cmd) {
            case RfidCardCmdCode.MF_ACTIVATE:
                int uidLength1 = datas[7];
                byte[] uid1 = new byte[uidLength1];
                System.arraycopy(datas, 8, uid1, 0, uidLength1);
                sendCardUid(uid1);
                break;
            case RfidCardCmdCode.MF_AUTHENT:

                break;
            case RfidCardCmdCode.MF_READ:
                Log.d("mf read", "success");
                break;
            case RfidCardCmdCode.MF_WRITE:
                Log.d("mf write", "success");
                break;
            case RfidCardCmdCode.PICE_AUTO_DETECT:
                if (datas[0] == 0x1f) {
                    sendCardUid(datas);
                } else {
                    Log.d("pice auto detect", "success");
                }
                break;

            default:
                if (datas[0] == 0x1f) {
                    sendCardUid(datas);
                }
                break;
        }
    }

    /**
     * 功能描述 : 发送数据命令
     *
     * @param : cmdClass 卡类命令
     * @param : cmdCode 命令类型
     * @param : info 发送数据
     */
    public synchronized byte[] sendCmd(byte cmdClass, byte cmdCode, byte[] info) {
        int infoLength = 0;
        if (info != null && info.length > 0) {
            infoLength = info.length;
        }
        byte[] datas = new byte[infoLength + 6];
        datas[0] = (byte) datas.length;
        datas[1] = (byte) cmdClass;
        datas[2] = (byte) cmdCode;
        datas[3] = (byte) infoLength;
        if (infoLength > 0) {
            for (int index = 0; index < infoLength; index++) {
                datas[index + 4] = info[index];
            }
        }
        byte BCC = 0x00;
        for (int i = 0; i < (datas[0] - 2); i++) {
            BCC ^= datas[i];
        }
        datas[datas[0] - 2] = (byte) ~BCC;
        datas[datas[0] - 1] = (byte) 0x03;
        mNowCmdCode = cmdCode;
        mAdapter.sendData(datas.length, datas);
        SystemClock.sleep(100);
        mAdapter.setReadStatus(1);
        return datas;
    }

    private void sendCardUid(byte[] datas) {
        if (datas[3] > 0) {
            int uidLength2 = datas[8];
            byte[] uid2 = new byte[uidLength2];
            System.arraycopy(datas, 9, uid2, 0, uidLength2);
            mCardUid = uid2;
            if (mRfidListener != null) {
                mRfidListener.detectCard(uid2);
            }
        }
    }

    public RfidListener getRfidListener() {
        return mRfidListener;
    }

    public void setRfidListener(RfidListener rfidListener) {
        this.mRfidListener = rfidListener;
    }

    public boolean isConnectDevice() {
        return isConnectDevice;
    }

    /**
     * 功能描述 : 发送读卡型号命令，判定rfid是否连接，有回应则连接成功。
     */
    private void setConnectDevice(boolean isConnectDevice) {
        this.isConnectDevice = isConnectDevice;
    }
}
