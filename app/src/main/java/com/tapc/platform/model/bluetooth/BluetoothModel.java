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

import com.tapc.platform.utils.IntentUtils;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/10/17.
 */

public class BluetoothModel {
    private Context mContext;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothA2dp mBTA2DP;
    private Listener mListener;
    private BroadcastReceiver mBluetoothReceiver;

    public BluetoothModel(Context context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public BluetoothAdapter getAdapter() {
        return mBluetoothAdapter;
    }

    public void start() {
        if (mBluetoothReceiver == null) {
            mBluetoothReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (mListener == null) {
                        return;
                    }
                    BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    switch (intent.getAction()) {
                        case BluetoothDevice.ACTION_FOUND:
                            mListener.actionFound(bluetoothDevice);
                            break;
                        case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                            mListener.actionBondStateChanged(bluetoothDevice);
                            break;
                        case BluetoothDevice.ACTION_ACL_CONNECTED:
                            mListener.actionAclConnected(bluetoothDevice);
                            break;
                        case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                            mListener.actionAclDisconnected(bluetoothDevice);
                            break;
                        case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                            mListener.actionDiscoveryStarted();
                            break;
                        case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                            mListener.actionDiscoveryFinished();
                            break;
                    }
                }
            };
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
        cancelDiscovery();
        IntentUtils.unregisterReceiver(mContext, mBluetoothReceiver);
        mBluetoothReceiver = null;
    }

    //max time : 300
    public void requestDiscoverable(int time) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, time);
        mContext.startActivity(intent);
    }

    public void setDiscoverableTimeout(int timeout) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class, int.class);
            setScanMode.setAccessible(true);

            setDiscoverableTimeout.invoke(adapter, timeout);
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isEnabled() {
        return mBluetoothAdapter.isEnabled();
    }

    public void enable() {
        mBluetoothAdapter.enable();
    }

    public void disable() {
        mBluetoothAdapter.disable();
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
    public void doPair(BluetoothDevice device) {
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
