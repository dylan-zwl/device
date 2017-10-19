package com.tapc.platform.model.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by Administrator on 2017/10/17.
 */

public class BluetoothModel {
    private Context mContext;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothA2dp mBTA2DP;
    private Listener mListener;
    private boolean isRegisterReceiver;

    public BluetoothModel(Context context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void start() {
        if (isRegisterReceiver == false) {
            isRegisterReceiver = true;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            mContext.registerReceiver(mBluetoothReceiver, intentFilter);
            mBluetoothAdapter.getProfileProxy(mContext, mProfileServiceListener, BluetoothProfile.A2DP);
        }
    }

    public void stop() {
        if (mBluetoothReceiver != null && isRegisterReceiver) {
            mContext.unregisterReceiver(mBluetoothReceiver);
            isRegisterReceiver = false;
        }
    }

    public boolean isEnabled() {
        return mBluetoothAdapter.isEnabled();
    }

    public void enable() {
        mBluetoothAdapter.enable();
    }

    public void startDiscovery() {
        mBluetoothAdapter.startDiscovery();
    }

    public void cancelDiscovery() {
        mBluetoothAdapter.cancelDiscovery();
    }

    public int getState() {
        return mBluetoothAdapter.getState();
    }

    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mListener == null) {
                return;
            }
            BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                mListener.actionFound(bluetoothDevice);
            } else if (intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                mListener.actionBondStateChanged(bluetoothDevice);
            } else if (intent.getAction().equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
                mListener.actionAclConnected(bluetoothDevice);
            } else if (intent.getAction().equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                mListener.actionAclDisconnected(bluetoothDevice);
            } else if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                mListener.actionDiscoveryStarted();
            } else if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                mListener.actionDiscoveryFinished();
            }
        }
    };

    public void setOnLitener(Listener listener) {
        mListener = listener;
    }

    public interface Listener {
        void actionFound(BluetoothDevice bluetoothDevice);

        void actionBondStateChanged(BluetoothDevice bluetoothDevice);

        void actionAclConnected(BluetoothDevice bluetoothDevice);

        void actionAclDisconnected(BluetoothDevice bluetoothDevice);

        void actionDiscoveryStarted();

        void actionDiscoveryFinished();
    }

    @SuppressLint("NewApi")
    private BluetoothProfile.ServiceListener mProfileServiceListener = new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            mBTA2DP = (BluetoothA2dp) proxy;
        }

        @Override
        public void onServiceDisconnected(int arg0) {
        }
    };

    /**
     * 配对
     */
    private void doPair(BluetoothDevice device) {
        if (device.getBluetoothClass().getMajorDeviceClass() == BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES) {
            try {
                mBTA2DP.getClass().getMethod("connect", BluetoothDevice.class).invoke(mBTA2DP, device);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        } else {
            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                try {
                    BluetoothDevice.class.getMethod("createBond").invoke(device);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
