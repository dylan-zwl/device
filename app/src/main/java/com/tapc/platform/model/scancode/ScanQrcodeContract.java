package com.tapc.platform.model.scancode;

import java.util.List;

import com.tapc.platform.entity.SportData;
import com.tapc.platform.entity.User;

public interface ScanQrcodeContract {
	interface View {
		void showQrcode(String qrcodeStr);

		void connectServerResult(boolean isSuccess);

		void openDevice(User user);

		User getUser();

		void recvSportPlan(int userId, List<SportData> plan_load);
	}

	interface Presenter {
		void connectServer();

		String getPassword();

		void setScanQrShowStatus(boolean isShow);
	}
}
