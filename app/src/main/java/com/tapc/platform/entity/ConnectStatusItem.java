package com.tapc.platform.entity;


/**
 * Created by Administrator on 2017/10/19.
 */

public class ConnectStatusItem {
    private String SSID;
    private String BSSID;
    private int connectedStatus;
    private int level;
    private int pwdType;

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public int getConnectedStatus() {
        return connectedStatus;
    }

    public void setConnectedStatus(int connectedStatus) {
        this.connectedStatus = connectedStatus;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public int getPwdType() {
        return pwdType;
    }

    public void setPwdType(int pwdType) {
        this.pwdType = pwdType;
    }
}
