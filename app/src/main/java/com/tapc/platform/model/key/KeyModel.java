package com.tapc.platform.model.key;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tapc.platform.library.controller.MachineStatusController;
import com.tapc.platform.utils.IntentUtils;


/**
 * Created by Administrator on 2017/3/24.
 */

public class KeyModel {
    private KeyListener mListener;
    private BroadcastReceiver mKeyboardReceiver;

    public interface KeyListener {
        void receverMcuKey(int key);
    }

    public void startListen(Context context, KeyListener listener) {
        if (mKeyboardReceiver == null) {
            mKeyboardReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int keycode = intent.getIntExtra(MachineStatusController.KEY_CODE, 0);
                    mListener.receverMcuKey(keycode);
                }
            };
            IntentUtils.registerReceiver(context, mKeyboardReceiver, MachineStatusController.DEVICE_KEY_EVENT);
        }
        this.mListener = listener;
    }

    public void stopListen(Context context) {
        IntentUtils.unregisterReceiver(context, mKeyboardReceiver);
        mKeyboardReceiver = null;
    }
}