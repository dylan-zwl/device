package com.tapc.platform.model.key;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tapc.platform.library.controller.MachineStatusController;
import com.tapc.platform.utils.IntentUtils;

import static com.tapc.platform.library.common.SystemSettings.mContext;

/**
 * Created by Administrator on 2017/3/24.
 */

public class KeyModel {
    private KeyListener mListener;
    private BroadcastReceiver mKeyboardReceiver;

    public interface KeyListener {
        void receverMcuKey(int key);
    }

    public void startListen(KeyListener listener) {
        if (mKeyboardReceiver == null) {
            mKeyboardReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int keycode = intent.getIntExtra(MachineStatusController.KEY_CODE, 0);
                    mListener.receverMcuKey(keycode);
                }
            };
            IntentUtils.registerReceiver(mContext, mKeyboardReceiver, MachineStatusController.DEVICE_KEY_EVENT);
        }
        this.mListener = listener;
    }

    public void stopListen() {
        IntentUtils.unregisterReceiver(mContext, mKeyboardReceiver);
        mKeyboardReceiver = null;
    }
}