package com.tapc.platform.model.tcp;

import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientInputThread extends Thread {
    private Socket mSocket;
    private boolean isStart = true;
    private InputStream mInputStream;
    private SocketListener mListener;

    private boolean isCanRead = true;

    public ClientInputThread(Socket socket) {
        this.mSocket = socket;
        try {
            mInputStream = socket.getInputStream();
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
        this.mListener = listener;
    }

    @Override
    public void run() {
        try {
            while (isStart) {
                if (isCanRead) {
                    int length = mInputStream.available();
                    if (length > 0) {
                        byte[] dataBuffer = new byte[length];
                        mInputStream.read(dataBuffer);
                        if (mListener != null) {
                            mListener.onMessage(dataBuffer);
                        }
                    }
                }
                SystemClock.sleep(5);
            }
            mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("tcp net", "clientInputThread close");
    }
}