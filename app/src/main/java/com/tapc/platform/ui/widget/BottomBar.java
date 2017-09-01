package com.tapc.platform.ui.widget;

import android.content.Context;

import com.tapc.platform.R;

/**
 * Created by Administrator on 2017/8/28.
 */

public class BottomBar extends BaseView {
    @Override
    protected int getContentView() {
        return R.layout.widget_bottom_bar;
    }

    public BottomBar(Context context) {
        super(context);
    }

}
