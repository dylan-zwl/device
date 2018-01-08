package com.tapc.platform.model.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.os.SystemClock;
import android.util.Log;

public class ClientInputThread extends Thread {
	private Socket socket;
	private boolean isStart = true;
	private InputStream ois;
	private SocketListener listener;

	private boolean isCanRead = true;

	public ClientInputThread(Socket socket) {
		this.socket = socket;
		try {
			ois = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public void setReadStatus(boolean vailable) {
		isCanRead = vailable;
	}

	public void setListener(SocketListener listener) {
		this.listener = listener;
	}

	@Override
	public void run() {
		try {
			while (isStart) {
				if (isCanRead) {
					byte[] data = new byte[1];
					List<Byte> dataBuffer = new ArrayList<>();
					while (data[0]!='\n') {
						ois.read(data);
						dataBuffer.add(data[0]);
					}
					byte[] dataSend = new byte[dataBuffer.size()];
					for (int i=0; i<dataBuffer.size(); i++) {
						dataSend[i] = dataBuffer.get(i);
					}
					if (listener != null) {
						listener.onMessage(dataSend);
					}
				}
			}
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d("tcp net", "clientInputThread close");
	}
}