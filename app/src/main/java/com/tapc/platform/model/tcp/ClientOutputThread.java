package com.tapc.platform.model.tcp;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientOutputThread extends Thread {
    private Socket mSocket;
    private OutputStream mOutputStream;
    private boolean isStart = true;
    private byte[] mDataBuffer;

    public ClientOutputThread(Socket socket) {
        this.mSocket = socket;
        try {
            mOutputStream = socket.getOutputStream();
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
        this.mDataBuffer = dataBuffer;
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public void run() {
        try {
            while (isStart) {
                if (mDataBuffer != null) {
                    mOutputStream.write(mDataBuffer);
                    mOutputStream.flush();
                    synchronized (this) {
                        wait();
                    }
                }
            }
            mOutputStream.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("tcp net", "clientOutputThread close");
    }

}