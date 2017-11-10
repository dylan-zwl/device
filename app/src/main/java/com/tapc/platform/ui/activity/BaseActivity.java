package com.tapc.platform.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tapc.platform.application.TapcApplication;
import com.trello.rxlifecycle2.components.RxActivity;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/5.
 */

public abstract class BaseActivity extends RxActivity {
    protected Context mContext;
    protected TapcApplication mTapcApp;

    protected abstract int getContentView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        mTapcApp = (TapcApplication) getApplication();
        mContext = this;
        initView();
    }

    protected void initView() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTapcApp.getRefWatcher().watch(this);
    }
}
