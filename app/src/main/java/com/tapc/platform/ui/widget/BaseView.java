package com.tapc.platform.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/28.
 */

public abstract class BaseView extends LinearLayout {
    protected Context mContext;

    protected abstract int getContentView();

    public BaseView(Context context) {
        super(context);
        init(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getContentView(), this, true);
        ButterKnife.bind(this);
        mContext = context;
        initView();
    }

    protected void initView() {
    }

    public void onDestroy() {
    }
}
