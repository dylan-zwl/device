package com.tapc.platform.ui.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;

import com.tapc.platform.R;
import com.tapc.platform.library.controller.MachineController;
import com.tapc.platform.utils.IntentUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tapc.platform.library.controller.MachineStatusController.DEVICE_ERROR_STATUS;
import static com.tapc.platform.library.controller.MachineStatusController.DEVICE_SAFE_KEY_STATUS;

/**
 * Created by Administrator on 2017/9/7.
 */

public class ErrorDialog extends BaseView {
    @BindView(R.id.error_safekey)
    LinearLayout mSafeKeyLL;
    @BindView(R.id.error_code)
    LinearLayout mErrorCodeLL;

    private boolean isHideError;
    private int mOldErrorStaus;

    private boolean isShowSafeKey = false;
    private boolean isShowError = false;

    public ErrorDialog(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_error;
    }

    public void init() {
//        IntentUtils.registerReceiver(mContext, mErrorReceiver, DEVICE_ERROR_STATUS);
        IntentUtils.registerReceiver(mContext, mSafeKeyReceiver, DEVICE_SAFE_KEY_STATUS);
        int safekey = MachineController.getInstance().getSafeKeyStatus();
        setSafeKeyShow(safekey);
    }

    @OnClick(R.id.error_left_tv)
    void leftOnClick() {
        isHideError = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isHideError = false;
            }
        }, 2000);
    }

    @OnClick(R.id.error_right_tv)
    void rightOnClick() {
        if (isHideError) {
            setVisibility(GONE);
            if (mErrorReceiver != null) {
                mContext.unregisterReceiver(mErrorReceiver);
            }
        }
    }

    private BroadcastReceiver mErrorReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(DEVICE_ERROR_STATUS, 0);
            status = status & 0xffff;
            Log.e(this.getClass().getName(), "error " + status);
            if (mOldErrorStaus == status) {
                return;
            }
            if (status == 0) {
//                mErrorCodeTx.setText("");
                mErrorCodeLL.setVisibility(GONE);
                isShowError = false;
            } else {
//                String errorStr = Integer.toHexString(status);
//                String text = String.format(mErrorCode, errorStr);
//                mErrorCodeTx.setText(text);
//                WorkoutBroadcase.send(mContext, DeviceWorkout.STOP);
                mErrorCodeLL.setVisibility(VISIBLE);
                isShowError = true;
            }
            resetDialogStatus();
            mOldErrorStaus = status;
        }
    };

    private BroadcastReceiver mSafeKeyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(DEVICE_SAFE_KEY_STATUS, 0);
            Log.e(this.getClass().getName(), "safekey " + status);
            setSafeKeyShow(status);
        }
    };

    private void setSafeKeyShow(int status) {
        if (status == 0) {
            mSafeKeyLL.setVisibility(GONE);
            isShowSafeKey = false;
        } else {
            mSafeKeyLL.setVisibility(VISIBLE);
            isShowSafeKey = true;
//            WorkoutBroadcase.send(mContext, DeviceWorkout.STOP);
        }
        resetDialogStatus();
    }

    private void resetDialogStatus() {
        if (isShowSafeKey || isShowError) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }
}
