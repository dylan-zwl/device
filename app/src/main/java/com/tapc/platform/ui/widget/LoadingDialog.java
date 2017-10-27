package com.tapc.platform.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.ui.view.BaseSystemView;
import com.tapc.platform.utils.WindowManagerUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/26.
 */

public class LoadingDialog extends BaseSystemView {
    @BindView(R.id.loading_pbar)
    ProgressBar mProgressBar;
    @BindView(R.id.loading_result)
    ImageButton mResult;
    @BindView(R.id.loading_cancel)
    ImageButton mCancel;
    @BindView(R.id.loading_tv)
    TextView mMsgTv;
    @BindView(R.id.loading_progress)
    TextView mProgressTv;

    @Override
    protected int getContentView() {
        return R.layout.dialog_loading;
    }

    public LoadingDialog(@NonNull Context context) {
        super(context.getApplicationContext());
    }

    @Override
    protected void initView() {
        super.initView();
    }

    public LoadingDialog setResult(boolean isSuccessed) {
        mCancel.setVisibility(View.VISIBLE);
        mProgressTv.setVisibility(View.GONE);
        if (isSuccessed) {
            mResult.setBackgroundResource(R.drawable.ic_result_success);
        } else {
            mResult.setBackgroundResource(R.drawable.ic_result_fail);
        }
        return this;
    }

    public LoadingDialog setMsgText(String text) {
        if (text == null) {
            return this;
        }
        mMsgTv.setText(text);
        return this;
    }

    public LoadingDialog setProgress(int progress) {
        if (!mProgressTv.isShown()) {
            mProgressTv.setVisibility(View.VISIBLE);
        }
        mProgressTv.setText(progress);
        return this;
    }

    public LoadingDialog setCancelVisibility(int visibility) {
        mCancel.setVisibility(View.VISIBLE);
        return this;
    }

    public void start() {
        mProgressTv.setText("");
        show();
    }

    public void stop() {
        dismiss();
    }

    @OnClick(R.id.loading_cancel)
    void cancelOnClick() {
        stop();
    }

    @Override
    public WindowManager.LayoutParams getLayoutParams() {
        return WindowManagerUtils.getLayoutParams(0, 0, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity
                .TOP);
    }
}
