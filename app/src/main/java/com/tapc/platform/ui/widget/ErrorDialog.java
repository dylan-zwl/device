package com.tapc.platform.ui.widget;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.tapc.platform.R;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/7.
 */

public class ErrorDialog extends BaseView {
    @BindView(R.id.error_safekey)
    LinearLayout mSafeKeyLL;

    public ErrorDialog(Context context) {
        super(context);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_error;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("#######", "##1####");
        return false;
    }
//
//    @OnClick(R.id.error_left_tv)
//    void leftOnClick() {
//        isHideError = true;
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                isHideError = false;
//            }
//        }, 2000);
//    }
//
//    @OnClick(R.id.error_right_tv)
//    void rightOnClick() {
//        if (isHideError) {
//            mErrorCodeRl.setVisibility(GONE);
//            if (!hasError()) {
//                setVisibility(GONE);
//            }
//            if (mErrorReceiver != null) {
//                mContext.unregisterReceiver(mErrorReceiver);
//            }
//        }
//    }
//
//    private BroadcastReceiver mErrorReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int status = intent.getIntExtra(HardwareStatusController.DEVICE_ERROR_STATUS, 0);
//            status = status & 0xffff;
//            Log.e(TAG, "error " + status);
//            if (mOldErrorStaus == status) {
//                return;
//            }
//            if (status == 0) {
//                mErrorCodeTx.setText("");
//                mErrorCodeRl.setVisibility(GONE);
//                hideDialog();
//            } else {
//                String errorStr = Integer.toHexString(status);
//                String text = String.format(mErrorCode, errorStr);
//                mErrorCodeTx.setText(text);
//                WorkoutBroadcase.send(mContext, DeviceWorkout.STOP);
//                mErrorCodeRl.setVisibility(VISIBLE);
//                setVisibility(VISIBLE);
//            }
//            mOldErrorStaus = status;
//        }
//    };
//
//    private BroadcastReceiver mSafeKeyReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int status = intent.getIntExtra(HardwareStatusController.DEVICE_SAFEKEY_STATUS, 0) &
//                    HardwareStatusController.SAFEKEY_MASK_VALUE;
//            Log.e("TAG", "safe key " + status);
//            setSafeKeyShow(status);
//        }
//    };
}
