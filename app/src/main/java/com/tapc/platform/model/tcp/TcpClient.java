package com.tapc.platform.model.tcp;

import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpClient {
    private String mIp;
    private int mPort;
    private Socket mSocket;
    private Client mClient;
    private SocketListener listener;
    private int mTimeOut;

    public TcpClient() {
    }

    public boolean connect(String ip, int port, int timeOut) {
        this.mIp = ip;
        this.mPort = port;
        mTimeOut = timeOut;
        boolean isConnected = false;
        try {
            mSocket = new Socket();
            mSocket.connect(new InetSocketAddress(mIp, mPort), mTimeOut);
            if (mSocket.isConnected()) {
                mClient = new Client(mSocket);
                mClient.setListener(listener);
                mClient.start();
                isConnected = true;
            } else {
                isConnected = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeSocket();
            isConnected = false;
        }
        setOpenStatus(isConnected);
        return isConnected;
    }

    public boolean isConnected() {
        if (mSocket != null && mSocket.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void setReadStatus(boolean vailable) {
        if (mSocket != null) {
            if (mClient != null) {
                mClient.getmIn().setReadStatus(vailable);
            }
        }
    }

    private void closeSocket() {
        if (mSocket != null && mSocket.isClosed() == false) {
            try {
                mSocket.close();
                Log.d("tcp net", "socket close success");
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("tcp net", "socket close failed");
            }
        }
        mSocket = null;
    }

    public void stop() {
        if (mClient != null) {
            mClient.getmIn().setStart(false);
            mClient.getmOut().setStart(false);
            mClient.getmIn().interrupt();
            mClient.getmOut().interrupt();
            SystemClock.sleep(100);
        }
        mClient = null;
        closeSocket();
    }

    public void sendData(String data) {
        if (data != null && !data.isEmpty() && mClient != null) {
            data += "\n";
            mClient.getmOut().sendMsg(data.getBytes());
        }
    }

    public void sendData(byte[] dataBuffer) {
        if (mClient != null) {
            mClient.getmOut().sendMsg(dataBuffer);
        }
    }

    private void setOpenStatus(boolean isConnect) {
        listener.onOpen(isConnect);
    }

    public SocketListener getListener() {
        return listener;
    }

    public void setListener(SocketListener listener) {
        this.listener = listener;
        if (mClient != null) {
            mClient.setListener(listener);
        }
    }

    public String getIp() {
        return mIp;
    }

    public void setIp(String ip) {
        this.mIp = ip;
    }

    public int getPort() {
        return mPort;
    }

    public void setPort(int port) {
        this.mPort = port;
    }

}
