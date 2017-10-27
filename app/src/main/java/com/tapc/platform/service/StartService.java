package com.tapc.platform.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.tapc.platform.R;
import com.tapc.platform.entity.WidgetShowStatus;
import com.tapc.platform.library.util.WorkoutEnum.WorkoutUpdate;
import com.tapc.platform.library.workouting.WorkOuting;
import com.tapc.platform.ui.activity.stop.StopActivity;
import com.tapc.platform.ui.widget.AlertDialog;
import com.tapc.platform.ui.widget.AppBar;
import com.tapc.platform.ui.widget.BottomBar;
import com.tapc.platform.ui.widget.CountdownDialog;
import com.tapc.platform.ui.widget.ErrorDialog;
import com.tapc.platform.ui.widget.GestureListener;
import com.tapc.platform.ui.widget.LoadingDialog;
import com.tapc.platform.ui.widget.ProgramStageDialog;
import com.tapc.platform.ui.widget.RunInforBar;
import com.tapc.platform.ui.widget.SettingTopBar;
import com.tapc.platform.ui.widget.ShortcutKey;
import com.tapc.platform.ui.widget.StartMenu;
import com.tapc.platform.utils.IntentUtils;

import java.util.Observable;
import java.util.Observer;

import static com.tapc.platform.library.common.SystemSettings.mContext;

/**
 * Created by Administrator on 2017/8/25.
 */

public class StartService extends Service implements Observer {
    private LocalBinder mBinder;
    private WindowManager mWindowManager;
    private StartMenu mStartMenu;
    private BottomBar mBottomBar;
    private AppBar mAppBar;
    private RunInforBar mRunInforBar;
    private ShortcutKey mShortcutKey;
    private GestureListener mGestureListener;
    private ProgramStageDialog mProgramStageDialog;
    private ErrorDialog mErrorDialog;
    private CountdownDialog mCountdownDialog;
    private SettingTopBar mSettingTopBar;
    private WorkOuting mWorkOuting;
    private AlertDialog mAlertDialog;
    private LoadingDialog mLoadingDialog;

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
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        setGestureListenerVisibility(true);
//        setProgramStageDialogVisibility(true);
//        setShortcutKeyVisibility(true);
//        initErrorDialog();
        mWorkOuting = WorkOuting.getInstance();
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

    public AlertDialog getAlertDialog() {
        return mAlertDialog;
    }

    public void setAlertDialogVisibility(WidgetShowStatus status) {
        switch (status) {
            case VISIBLE:
                if (mAlertDialog == null) {
                    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager
                            .LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                                    | WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING
                                    | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                            PixelFormat.TRANSPARENT);
                    params.gravity = Gravity.TOP;
                    params.x = 0;
                    params.y = 0;
                    mAlertDialog = new AlertDialog(this);
                    mWindowManager.addView(mAlertDialog, params);
                } else {
                    mAlertDialog.setVisibility(View.VISIBLE);
                }
                break;
            case GONE:
                if (mAlertDialog != null) {
                    mAlertDialog.setVisibility(View.GONE);
                }
                break;
            case REMOVE:
                if (mAlertDialog != null) {
                    mWindowManager.removeView(mAlertDialog);
                    mAlertDialog.onDestroy();
                    mAlertDialog = null;
                }
                break;
        }
    }

    public void setLoadingDialogVisibility(WidgetShowStatus status) {
        switch (status) {
            case VISIBLE:
                if (mLoadingDialog == null) {
                    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager
                            .LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                                    | WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING
                                    | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                            PixelFormat.TRANSPARENT);
                    params.gravity = Gravity.TOP;
                    params.x = 0;
                    params.y = 0;
                    mLoadingDialog = new LoadingDialog(this);
                    mWindowManager.addView(mLoadingDialog, params);
                } else {
                    mLoadingDialog.setVisibility(View.VISIBLE);
                }
                break;
            case GONE:
                if (mLoadingDialog != null) {
                    mLoadingDialog.setVisibility(View.GONE);
                }
                break;
            case REMOVE:
                if (mLoadingDialog != null) {
                    mWindowManager.removeView(mLoadingDialog);
                    mLoadingDialog.onDestroy();
                    mLoadingDialog = null;
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

    public void setSettingTopBarVisibility(WidgetShowStatus status) {
        switch (status) {
            case VISIBLE:
                if (mSettingTopBar == null) {
                    final WindowManager.LayoutParams bottomBarParams = new WindowManager.LayoutParams(
                            WindowManager.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen
                            .setting_top_bar_h),
                            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                                    | WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING
                                    | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                            PixelFormat.TRANSPARENT);
                    bottomBarParams.gravity = Gravity.TOP | Gravity.CENTER_VERTICAL;
                    bottomBarParams.x = 0;
                    bottomBarParams.y = 0;
                    mSettingTopBar = new SettingTopBar(this);
                    mWindowManager.addView(mSettingTopBar, bottomBarParams);
                } else {
                    mSettingTopBar.setVisibility(View.VISIBLE);
                }
                break;
            case GONE:
                if (mSettingTopBar != null) {
                    mSettingTopBar.setVisibility(View.GONE);
                }
                break;
            case REMOVE:
                if (mSettingTopBar != null) {
                    mWindowManager.removeView(mSettingTopBar);
                    mSettingTopBar.onDestroy();
                    mSettingTopBar = null;
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

    private void initErrorDialog() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager
                .LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSPARENT);
        params.gravity = Gravity.TOP;
        params.x = 0;
        params.y = 0;
        mErrorDialog = new ErrorDialog(this);
        mErrorDialog.init();
        mWindowManager.addView(mErrorDialog, params);
    }

    private void removeErrorDialog() {
        if (mErrorDialog != null) {
            mWindowManager.removeView(mErrorDialog);
            mErrorDialog = null;
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
                    mWorkOuting.subscribeObserverNotification(this);
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
                    mWorkOuting.unsubscribeObserverNotification(this);
                }
                break;
        }
    }

    public void setCountDownVisibility(WidgetShowStatus status) {
        switch (status) {
            case VISIBLE:
                if (mCountdownDialog == null) {
                    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager
                            .LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                                    | WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING
                                    | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                            PixelFormat.TRANSPARENT);
                    params.gravity = Gravity.TOP;
                    params.x = 0;
                    params.y = 0;
                    mCountdownDialog = new CountdownDialog(this);
                    mWindowManager.addView(mCountdownDialog, params);
                } else {
                    mCountdownDialog.setVisibility(View.VISIBLE);
                }
                break;
            case GONE:
                if (mCountdownDialog != null) {
                    mCountdownDialog.setVisibility(View.GONE);
                }
                break;
            case REMOVE:
                if (mCountdownDialog != null) {
                    mWindowManager.removeView(mCountdownDialog);
                    mCountdownDialog.onDestroy();
                    mCountdownDialog = null;
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeErrorDialog();
    }

    private void stopDevice() {
        setAppBarVisibility(WidgetShowStatus.REMOVE);
        setRunInforBarVisibility(WidgetShowStatus.REMOVE);
        setProgramStageDialogVisibility(WidgetShowStatus.REMOVE);
        setShortcutKeyVisibility(WidgetShowStatus.REMOVE);
        setBottomBarVisibility(WidgetShowStatus.REMOVE);
        IntentUtils.startActivity(mContext, StopActivity.class, null, Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public void update(Observable o, Object arg) {
        WorkoutUpdate workoutUpdate = (WorkoutUpdate) arg;
        if (workoutUpdate != null) {
            switch (workoutUpdate) {
                case UI_STOP:
                    stopDevice();
                    break;
                case UI_RESUME:
                    setCountDownVisibility(WidgetShowStatus.REMOVE);
                    break;
            }
        }
    }
}
