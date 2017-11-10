package com.tapc.platform.model.tcp;

import java.net.Socket;

public class Client {

    private ClientInputThread mIn;
    private ClientOutputThread mOut;

    public Client(Socket socket) {
        mIn = new ClientInputThread(socket);
        mOut = new ClientOutputThread(socket);
    }

    public void start() {
        mIn.setStart(true);
        mOut.setStart(true);
        mIn.start();
        mOut.start();
    }

    public void setListener(SocketListener listener) {
        if (mIn != null) {
            mIn.setListener(listener);
        }
    }

    public ClientInputThread getIn() {
        return mIn;
    }

    public ClientOutputThread getOut() {
        return mOut;
    }
}