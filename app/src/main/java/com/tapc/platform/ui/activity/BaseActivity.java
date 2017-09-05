package com.tapc.platform.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/5.
 */

public abstract class BaseActivity extends Activity {
    protected Context mContext;

    protected abstract int getContentView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    protected void initView() {
    }
}
