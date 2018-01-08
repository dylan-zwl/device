package com.tapc.platform.model.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import android.util.Log;

public class ClientOutputThread extends Thread {
	private Socket socket;
	private OutputStream dos;
	private boolean isStart = true;
	private byte[] dataBuffer;

	public ClientOutputThread(Socket socket) {
		this.socket = socket;
		try {
			dos = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
		if (!isStart) {
			synchronized (this) {
				notifyAll();
			}
		}
	}

	public void sendMsg(byte[] dataBuffer) {
		this.dataBuffer = dataBuffer;
		synchronized (this) {
			notifyAll();
		}
	}

	@Override
	public void run() {
		try {
			while (isStart) {
				if (dataBuffer != null) {
					dos.write(dataBuffer);
					dos.flush();
					synchronized (this) {
						wait();
					}
				}
			}
			dos.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d("tcp net", "clientOutputThread close");
	}

}