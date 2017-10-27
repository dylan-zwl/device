package com.tapc.platform.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.ui.view.BaseSystemView;
import com.tapc.platform.utils.WindowManagerUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/26.
 */

public class AlertDialog extends BaseSystemView {
    @BindView(R.id.alert_title)
    TextView mTitleTv;
    @BindView(R.id.alert_msg)
    TextView mMsgTv;
    @BindView(R.id.alert_positive)
    Button mPositiveBtn;
    @BindView(R.id.alert_negative)
    Button mNegativeBtn;

    private Listener mListener;
    private Handler mHandler;

    @Override
    protected int getContentView() {
        return R.layout.dialog_alert;
    }

    public AlertDialog(@NonNull Context context) {
        super(context.getApplicationContext());
        mHandler = new Handler();
    }

    public AlertDialog setTitle(String title) {
        if (title == null) {
            return this;
        }
        mTitleTv.setText(title);
        return this;
    }

    public AlertDialog setMsgText(String text) {
        if (text == null) {
            return this;
        }
        mMsgTv.setText(text);
        return this;
    }

    public AlertDialog setButtonVisibility(int visibility) {
        mPositiveBtn.setVisibility(visibility);
        mNegativeBtn.setVisibility(visibility);
        return this;
    }

    public AlertDialog setPositiveText(String text) {
        if (text == null) {
            return this;
        }
        mPositiveBtn.setText(text);
        return this;
    }

    public AlertDialog setNegativeText(String text) {
        if (text == null) {
            return this;
        }
        mNegativeBtn.setText(text);
        return this;
    }

    public AlertDialog setTimeOut(int delayMs) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, delayMs);
        return this;
    }

    public AlertDialog setOnClickListener(Listener listener) {
        mListener = listener;
        return this;
    }

    public interface Listener {
        void positiveOnCick();

        void negativeOnClick();
    }


    @OnClick(R.id.alert_positive)
    void positive() {
        if (mListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.positiveOnCick();
                    dismiss();
                }
            });
        }
    }

    @OnClick(R.id.alert_negative)
    void negative() {
        if (mListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.negativeOnClick();
                    dismiss();
                }
            });
        }
    }

    @Override
    public WindowManager.LayoutParams getLayoutParams() {
        return WindowManagerUtils.getLayoutParams(0, 0, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity
                .TOP);
    }
}
