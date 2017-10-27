package com.tapc.platform.utils;

import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * Created by Administrator on 2017/10/27.
 */

public class WindowManagerUtils {

    public static LayoutParams getLayoutParams(int x, int y, int w, int h, int gravity) {
        final LayoutParams params = new WindowManager.LayoutParams(w, h, LayoutParams.TYPE_SYSTEM_ALERT, LayoutParams
                .FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_LAYOUT_IN_SCREEN | LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING |
                LayoutParams.FLAG_SPLIT_TOUCH, PixelFormat.TRANSPARENT);
        params.gravity = gravity;
        params.x = x;
        params.y = y;
        return params;
    }

    public static void addView(WindowManager windowManager, View view, LayoutParams params) {
        windowManager.addView(view, params);
    }

}
