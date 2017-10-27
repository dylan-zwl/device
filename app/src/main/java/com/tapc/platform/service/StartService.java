package com.tapc.platform.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.tapc.platform.entity.WidgetShowStatus;
import com.tapc.platform.library.util.WorkoutEnum.WorkoutUpdate;
import com.tapc.platform.library.workouting.WorkOuting;
import com.tapc.platform.ui.activity.stop.StopActivity;
import com.tapc.platform.ui.widget.AppBar;
import com.tapc.platform.ui.widget.BottomBar;
import com.tapc.platform.ui.widget.CountdownDialog;
import com.tapc.platform.ui.widget.ErrorDialog;
import com.tapc.platform.ui.widget.GestureListener;
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

    public StartMenu getStartMenu() {
        if (mStartMenu == null) {
            mStartMenu = new StartMenu(this);
        }
        return mStartMenu;
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

    public void startDevice() {
        if (mShortcutKey == null) {
            mShortcutKey = new ShortcutKey(this);
        }
        mShortcutKey.show();
        WorkOuting.getInstance().subscribeObserverNotification(this);
    }

    private void stopDevice() {
        if (mShortcutKey != null) {
            mShortcutKey.dismiss();
        }
        WorkOuting.getInstance().unsubscribeObserverNotification(this);
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
