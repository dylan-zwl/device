package com.tapc.platform.ui.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.library.workouting.WorkOuting;
import com.tapc.platform.ui.view.BaseSystemView;
import com.tapc.platform.utils.IntentUtils;
import com.tapc.platform.utils.WindowManagerUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tapc.platform.library.controller.MachineStatusController.DEVICE_ERROR_STATUS;
import static com.tapc.platform.library.controller.MachineStatusController.DEVICE_SAFE_KEY_STATUS;

/**
 * Created by Administrator on 2017/9/7.
 */

public class ErrorDialog extends BaseSystemView {
    @BindView(R.id.error_safekey)
    LinearLayout mSafeKeyLL;
    @BindView(R.id.error_code)
    RelativeLayout mErrorCodeLL;
    @BindView(R.id.error_code_tx)
    TextView mErrorCodeTx;

    private boolean isHideError;
    private int mOldErrorStaus;

    private boolean isShowSafeKey = false;
    private boolean isShowError = false;

    public ErrorDialog(Context context) {
        super(context);
//        IntentUtils.registerReceiver(mContext, mErrorReceiver, DEVICE_ERROR_STATUS);
//        IntentUtils.registerReceiver(mContext, mSafeKeyReceiver, DEVICE_SAFE_KEY_STATUS);
    }

    @Override
    protected void initView() {
        super.initView();
//        int safekey = MachineController.getInstance().getSafeKeyStatus();
//        setSafeKeyShow(safekey);
    }

    @Override
    public WindowManager.LayoutParams getLayoutParams() {
        return WindowManagerUtils.getLayoutParams(0, 0, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity
                .TOP);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_error;
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
            mErrorCodeLL.setVisibility(GONE);
            isShowError = false;
            if (mErrorReceiver != null) {
                IntentUtils.unregisterReceiver(mContext, mErrorReceiver);
            }
            resetDialogStatus();
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
                mErrorCodeTx.setText("");
                mErrorCodeLL.setVisibility(GONE);
                isShowError = false;
            } else {
//                String errorStr = Integer.toHexString(status);
                String text = String.format("%x", status);
                mErrorCodeTx.setText(text);
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
            WorkOuting.getInstance().stop();
        }
        resetDialogStatus();
    }

    private void resetDialogStatus() {
        if (isShowSafeKey || isShowError) {
            show();
        } else {
            dismiss();
        }
    }
}
