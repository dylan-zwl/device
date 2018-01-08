package com.tapc.platform.model.scancode;

import java.util.List;

import android.content.Context;

import com.tapc.platform.activity.SportMenuActivity;
import com.tapc.platform.entity.SportData;
import com.tapc.platform.entity.User;
import com.tapc.platform.modules.scancode.ScanQrcodeModel.TcpListener;

public class ScanQrcodePresenter implements ScanQrcodeContract.Presenter {
	private ScanQrcodeContract.View mView;
	private ScanQrcodeModel mModel;

	public ScanQrcodePresenter(Context context, ScanQrcodeContract.View view) {
		mView = view;
		mModel = new ScanQrcodeModel();
		mModel.init(context);
		mModel.setTcpListener(mTcpListener);
	}

	@Override
	public void connectServer() {
		mModel.connectServer();
	}

	private TcpListener mTcpListener = new TcpListener() {

		@Override
		public void showQrcode(String qrcodeStr) {
			mView.showQrcode(qrcodeStr);
		}

		@Override
		public void openDevice(User user) {
			mView.openDevice(user);
		}

		@Override
		public void connectServerResult(boolean isSuccess) {
			mView.connectServerResult(isSuccess);
		}

		@Override
		public User getUser() {
			return mView.getUser();
		}

		@Override
		public void recvSportPlan(int userId, List<SportData> plan_load) {
			mView.recvSportPlan(userId, plan_load);
		}
	};

	@Override
	public String getPassword() {
		return mModel.getLoginPassword();
	}

	@Override
	public void setScanQrShowStatus(boolean isShow) {
		mModel.setScanQrShowStatus(isShow);
	}
}
