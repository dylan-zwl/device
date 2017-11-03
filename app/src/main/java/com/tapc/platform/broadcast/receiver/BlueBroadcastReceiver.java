package com.tapc.platform.broadcast.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tapc.platform.entity.BluetoothConnectStatus;
import com.tapc.platform.utils.RxBus;

public class BlueBroadcastReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_ACL_CONNECTED == action) {
            RxBus.getsInstance().post(new BluetoothConnectStatus(true));
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED == action) {
            RxBus.getsInstance().post(new BluetoothConnectStatus(false));
        }
    }
}
