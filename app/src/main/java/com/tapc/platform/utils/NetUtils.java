package com.tapc.platform.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Administrator on 2017/9/8.
 */

public class NetUtils {
    private NetUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 功能描述 : 判断网络是否连接
     *
     * @param :
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 功能描述 : 判断网络连接类型
     *
     * @param :
     */
    public static boolean isOfConnectType(Context context, int type) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (connectivityManager.getActiveNetworkInfo().getType() == type) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 功能描述 : 获取mac地址
     *
     * @param :
     */
    public static String getLocalMacAddress(Context context) {
        String macAddress = "";
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        try {
            macAddress = info.getMacAddress().replace(":", "");
        } catch (Exception e) {
        }
        if (macAddress == null) {
            macAddress = "";
        }
        return macAddress;
    }
}