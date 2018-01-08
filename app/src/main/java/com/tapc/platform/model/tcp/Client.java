package com.tapc.platform.model.tcp;

import java.net.Socket;

public class Client {

	private ClientInputThread in;
	private ClientOutputThread out;

	public Client(Socket socket) {
		in = new ClientInputThread(socket);
		out = new ClientOutputThread(socket);
	}

	public void start() {
		in.setStart(true);
		out.setStart(true);
		in.start();
		out.start();
	}

	public void setListener(SocketListener listener) {
		if (in != null) {
			in.setListener(listener);
		}
	}

	public ClientInputThread getIn() {
		return in;
	}

	public ClientOutputThread getOut() {
		return out;
	}
}