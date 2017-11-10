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
    private int mTimeOut;
    private SocketListener mListener;

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
                mClient.start();
                isConnected = true;
            } else {
                isConnected = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeSocket();
            if (mListener != null) {
                mListener.onError(e);
            }
            isConnected = false;
        }
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
                mClient.getIn().setReadStatus(vailable);
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

    public void close() {
        if (mClient != null) {
            mClient.getIn().setStart(false);
            mClient.getOut().setStart(false);
            mClient.getIn().interrupt();
            mClient.getOut().interrupt();
            SystemClock.sleep(100);
        }
        mClient = null;
        closeSocket();
    }

    public void sendData(String data) {
        if (data != null && !data.isEmpty() && mClient != null) {
            data += "\n";
            mClient.getOut().sendMsg(data.getBytes());
        }
    }

    public void sendData(byte[] dataBuffer) {
        if (mClient != null) {
            mClient.getOut().sendMsg(dataBuffer);
        }
    }

    public void setListener(SocketListener listener) {
        if (mClient != null && listener != null) {
            mListener = listener;
            mClient.setListener(listener);
        }
    }

    public String getIp() {
        return mIp;
    }

    public int getPort() {
        return mPort;
    }
}
