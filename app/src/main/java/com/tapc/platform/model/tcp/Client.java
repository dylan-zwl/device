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
            mIn.setmListener(listener);
        }
    }

    public ClientInputThread getmIn() {
        return mIn;
    }

    public ClientOutputThread getmOut() {
        return mOut;
    }
}