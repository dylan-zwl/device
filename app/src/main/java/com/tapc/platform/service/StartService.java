package com.tapc.platform.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.tapc.platform.R;
import com.tapc.platform.application.TapcApplication;
import com.tapc.platform.entity.WidgetShowStatus;
import com.tapc.platform.library.common.TreadmillSystemSettings;
import com.tapc.platform.library.controller.MachineController;
import com.tapc.platform.library.data.TreadmillProgramSetting;
import com.tapc.platform.library.util.WorkoutEnum;
import com.tapc.platform.library.workouting.WorkOuting;
import com.tapc.platform.ui.widget.AppBar;
import com.tapc.platform.ui.widget.BottomBar;
import com.tapc.platform.ui.widget.GestureListener;
import com.tapc.platform.ui.widget.ProgramStageDialog;
import com.tapc.platform.ui.widget.RunInforBar;
import com.tapc.platform.ui.widget.ShortcutKey;
import com.tapc.platform.ui.widget.StartMenu;

import static com.tapc.platform.library.common.SystemSettings.mContext;

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
    private ShortcutKey mShortcutKey;
    private GestureListener mGestureListener;
    private ProgramStageDialog mProgramStageDialog;

    private WorkOuting mWorkOuting;

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
//        setProgramStageDialogVisibility(true);
//        setShortcutKeyVisibility(true);
    }

    public boolean isStartMenuShown() {
        if (mStartMenu != null && mStartMenu.isShown()) {
            return true;
        }
        return false;
    }

    public void setStartMenuVisibility(WidgetShowStatus status) {
        switch (status) {
            case VISIBLE:
                if (mStartMenu == null) {
                    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager
                            .LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                                    | WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING
                                    | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                            PixelFormat.TRANSPARENT);
                    params.gravity = Gravity.RIGHT;
                    params.x = 0;
                    params.y = 0;
                    mStartMenu = new StartMenu(this);
                    mWindowManager.addView(mStartMenu, params);
                } else {
                    mStartMenu.setVisibility(View.VISIBLE);
                }
                break;
            case GONE:
                if (mStartMenu != null) {
                    mStartMenu.setVisibility(View.GONE);
                }
                break;
            case REMOVE:
                if (mStartMenu != null) {
                    mWindowManager.removeView(mStartMenu);
                    mStartMenu.onDestroy();
                    mStartMenu = null;
                }
                break;
        }
    }

    public boolean isBottomBarShown() {
        if (mBottomBar != null && mBottomBar.isShown()) {
            return true;
        }
        return false;
    }

    public void setBottomBarVisibility(WidgetShowStatus status) {
        switch (status) {
            case VISIBLE:
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
                } else {
                    mBottomBar.setVisibility(View.VISIBLE);
                }
                break;
            case GONE:
                if (mBottomBar != null) {
                    mBottomBar.setVisibility(View.GONE);
                }
                break;
            case REMOVE:
                if (mBottomBar != null) {
                    mWindowManager.removeView(mBottomBar);
                    mBottomBar.onDestroy();
                    mBottomBar = null;
                }
                break;
        }
    }

    public boolean isAppBarShown() {
        if (mAppBar != null && mAppBar.isShown()) {
            return true;
        }
        return false;
    }

    public void setAppBarVisibility(WidgetShowStatus status) {
        switch (status) {
            case VISIBLE:
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
                    appBarParams.y = 132;
                    mAppBar = new AppBar(this, mWindowManager, appBarParams);
                    mWindowManager.addView(mAppBar, appBarParams);
                } else {
                    mAppBar.setVisibility(View.VISIBLE);
                }
                break;
            case GONE:
                if (mAppBar != null) {
                    mAppBar.setVisibility(View.GONE);
                }
                break;
            case REMOVE:
                if (mAppBar != null) {
                    mWindowManager.removeView(mAppBar);
                    mAppBar.onDestroy();
                    mAppBar = null;
                }
                break;
        }
    }

    public boolean isRunInforBarShown() {
        if (mRunInforBar != null && mRunInforBar.isShown()) {
            return true;
        }
        return false;
    }

    public void setRunInforBarVisibility(WidgetShowStatus status) {
        switch (status) {
            case VISIBLE:
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
                    mRunInforBar = new RunInforBar(this, mWindowManager, runInforBarParams);
                    mWindowManager.addView(mRunInforBar, runInforBarParams);
                } else {
                    mRunInforBar.setVisibility(View.VISIBLE);
                }
                break;
            case GONE:
                if (mRunInforBar != null) {
                    mRunInforBar.setVisibility(View.GONE);
                }
                break;
            case REMOVE:
                if (mRunInforBar != null) {
                    mWindowManager.removeView(mRunInforBar);
                    mRunInforBar.onDestroy();
                    mRunInforBar = null;
                }
                break;
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

    public boolean isProgramStageDialogShown() {
        if (mProgramStageDialog != null && mProgramStageDialog.isShown()) {
            return true;
        }
        return false;
    }

    public void setProgramStageDialogVisibility(WidgetShowStatus status) {
        switch (status) {
            case VISIBLE:
                if (mProgramStageDialog == null) {
                    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager
                            .LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                                    | WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING
                                    | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                            PixelFormat.TRANSPARENT);
                    params.gravity = Gravity.LEFT | Gravity.TOP;
                    params.x = 36;
                    params.y = 132;
                    mProgramStageDialog = new ProgramStageDialog(this);
                    mWindowManager.addView(mProgramStageDialog, params);
                } else {
                    mProgramStageDialog.setVisibility(View.VISIBLE);
                }
                break;
            case GONE:
                if (mProgramStageDialog != null) {
                    mProgramStageDialog.setVisibility(View.GONE);
                }
                break;
            case REMOVE:
                if (mProgramStageDialog != null) {
                    mWindowManager.removeView(mProgramStageDialog);
                    mProgramStageDialog.onDestroy();
                    mProgramStageDialog = null;
                }
                break;
        }
    }

    public void setShortcutKeyVisibility(WidgetShowStatus status) {
        switch (status) {
            case VISIBLE:
                if (mShortcutKey == null) {
                    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager
                            .LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                                    | WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING
                                    | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                            PixelFormat.TRANSPARENT);
                    params.gravity = Gravity.TOP | Gravity.LEFT;
                    params.x = 36;
                    params.y = 726;
                    mShortcutKey = new ShortcutKey(this, mWindowManager, params);
                    mWindowManager.addView(mShortcutKey, params);
                } else {
                    mShortcutKey.setVisibility(View.VISIBLE);
                }
                break;
            case GONE:
                if (mShortcutKey != null) {
                    mShortcutKey.setVisibility(View.GONE);
                }
                break;
            case REMOVE:
                if (mShortcutKey != null) {
                    mWindowManager.removeView(mShortcutKey);
                    mShortcutKey.onDestroy();
                    mShortcutKey = null;
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
