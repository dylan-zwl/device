package com.tapc.platform.model.tcp;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

    public void setmListener(SocketListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void run() {
        try {
            while (isStart) {
                if (isCanRead) {
                    byte[] data = new byte[1];
                    List<Byte> dataBuffer = new ArrayList<>();
                    while (data[0] != '\n') {
                        mInputStream.read(data);
                        dataBuffer.add(data[0]);
                    }
                    byte[] dataSend = new byte[dataBuffer.size()];
                    for (int i = 0; i < dataBuffer.size(); i++) {
                        dataSend[i] = dataBuffer.get(i);
                    }
                    if (mListener != null) {
                        mListener.onMessage(dataSend);
                    }
                }
            }
            mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("tcp net", "clientInputThread close");
    }
}