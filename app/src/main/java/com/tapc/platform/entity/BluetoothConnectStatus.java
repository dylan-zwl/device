package com.tapc.platform.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/8.
 */

public class BluetoothConnectStatus implements Serializable {
    private boolean isConnected;

    public BluetoothConnectStatus(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
