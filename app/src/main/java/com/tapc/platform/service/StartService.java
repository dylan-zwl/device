package com.tapc.platform.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;

import com.tapc.platform.R;
import com.tapc.platform.ui.widget.AppBar;
import com.tapc.platform.ui.widget.BottomBar;
import com.tapc.platform.ui.widget.GestureListener;
import com.tapc.platform.ui.widget.RunInforBar;
import com.tapc.platform.ui.widget.StartMenu;

/**
 * Created by Administrator on 2017/8/25.
 */

public class StartService extends Service {
    private LocalBinder mBinder;
    private WindowManager mWindowManager;
    private StartMenu mStartMenu;
    private BottomBar mBottomBar;
    private AppBar mAppBar;
    private RunInforBar mRunInforBar;
    private GestureListener mGestureListener;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initView();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initView() {
        mBinder = new LocalBinder(this);
        mWindowManager = (WindowManager) getSystemService("window");
//        setGestureListenerVisibility(true);
    }

    public void setStartMenuVisibility(boolean visibility) {
        if (visibility) {
            if (mStartMenu == null) {
                final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        480, WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                                | WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING
                                | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                        PixelFormat.TRANSPARENT);
                params.gravity = Gravity.RIGHT | Gravity.CENTER_HORIZONTAL;
                params.x = 0;
                params.y = 0;
                mStartMenu = new StartMenu(this);
                mWindowManager.addView(mStartMenu, params);
            }
        } else {
            if (mStartMenu != null) {
                mWindowManager.removeView(mStartMenu);
                mStartMenu = null;
            }
        }
    }

    public void setBottomBarVisibility(boolean visibility) {
        if (visibility) {
            if (mBottomBar == null) {
                final WindowManager.LayoutParams bottomBarParams = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen
                        .bottom_bar_h),
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                                | WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING
                                | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                        PixelFormat.TRANSPARENT);
                bottomBarParams.gravity = Gravity.BOTTOM | Gravity.CENTER_VERTICAL;
                bottomBarParams.x = 0;
                bottomBarParams.y = 0;
                mBottomBar = new BottomBar(this);
                mWindowManager.addView(mBottomBar, bottomBarParams);
            }
        } else {
            if (mBottomBar != null) {
                mWindowManager.removeView(mBottomBar);
                mBottomBar = null;
            }
        }
    }

    public void setAppBarVisibility(boolean visibility) {
        if (visibility) {
            if (mAppBar == null) {
                final WindowManager.LayoutParams appBarParams = new WindowManager.LayoutParams(WindowManager
                        .LayoutParams
                        .WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                                | WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING
                                | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                        PixelFormat.TRANSPARENT);
                appBarParams.gravity = Gravity.RIGHT | Gravity.TOP;
                appBarParams.x = 0;
                appBarParams.y = 0;
                mAppBar = new AppBar(this, mWindowManager, appBarParams);
                mWindowManager.addView(mAppBar, appBarParams);
            }
        } else {
            if (mAppBar != null) {
                mAppBar.onDestroy();
                mWindowManager.removeView(mAppBar);
                mAppBar = null;
            }
        }
    }

    public void setRunInforBarVisibility(boolean visibility) {
        if (visibility) {
            if (mRunInforBar == null) {
                final WindowManager.LayoutParams runInforBarParams = new WindowManager.LayoutParams(WindowManager
                        .LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                                | WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING
                                | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                        PixelFormat.TRANSPARENT);
                runInforBarParams.gravity = Gravity.TOP;
                runInforBarParams.x = 0;
                runInforBarParams.y = 0;
                mRunInforBar = new RunInforBar(this);
                mWindowManager.addView(mRunInforBar, runInforBarParams);
            }
        } else {
            if (mRunInforBar != null) {
                mWindowManager.removeView(mRunInforBar);
                mRunInforBar = null;
            }
        }
    }

    public void setGestureListenerVisibility(boolean visibility) {
        if (visibility) {
            if (mGestureListener == null) {
                final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager
                        .LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                        PixelFormat.TRANSPARENT);
                mGestureListener = new GestureListener(this);
                mWindowManager.addView(mGestureListener, params);
            }
        } else {
            if (mGestureListener != null) {
                mWindowManager.removeView(mGestureListener);
                mGestureListener = null;
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
