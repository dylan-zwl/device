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
    @BindView(R.id.gesture_ll)
    LinearLayout mLinearLayout;

    public ErrorDialog(Context context) {
        super(context);
//        mShortcutKeyLL.setOnTouchListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.widget_gesture_listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("#######", "##1####");
        return false;
    }
}
