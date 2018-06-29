package com.tapc.platform.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.tapc.platform.library.abstractset.WorkoutInfo;
import com.tapc.platform.library.workouting.WorkOuting;
import com.tapc.platform.library.workouting.WorkoutUpdateObserver;
import com.tapc.platform.ui.activity.stop.StopActivity;
import com.tapc.platform.ui.widget.CountdownDialog;
import com.tapc.platform.ui.widget.ErrorDialog;
import com.tapc.platform.ui.widget.ShortcutKey;
import com.tapc.platform.ui.widget.StartMenu;
import com.tapc.platform.utils.IntentUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/8/25.
 */

public class StartService extends Service implements Observer {
    private LocalBinder mBinder;
    private StartMenu mStartMenu;
    private ShortcutKey mShortcutKey;
    private ErrorDialog mErrorDialog;
    private CountdownDialog mCountdownDialog;

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
        initErrorDialog();
    }

    public StartMenu getStartMenu() {
        if (mStartMenu == null) {
            mStartMenu = new StartMenu(this);
        }
        return mStartMenu;
    }

    public void initErrorDialog() {
        mErrorDialog = new ErrorDialog(this);

    }

    public ShortcutKey getShortcutKey() {
        return mShortcutKey;
    }

    public CountdownDialog getCountdownDialog() {
        if (mCountdownDialog == null) {
            mCountdownDialog = new CountdownDialog(this);
        }
        return mCountdownDialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void startDevice() {
        if (mShortcutKey == null) {
            mShortcutKey = new ShortcutKey(this);
        }
        mShortcutKey.show();
        WorkOuting.getInstance().subscribeObserverNotification(this);
    }

    public void stopDevice() {
        if (mShortcutKey != null) {
            mShortcutKey.dismiss();
            mShortcutKey = null;
        }
        WorkOuting.getInstance().unSubscribeObserverNotification(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        WorkoutUpdateObserver workoutUpdateObserver = (WorkoutUpdateObserver) arg;
        if (workoutUpdateObserver != null) {
            switch (workoutUpdateObserver.getWorkoutUpdate()) {
                case UI_STOP:
                    break;
                case UI_RESUME:
                    getCountdownDialog().dismiss();
                    break;
                case UI_SAVE_RESULT:
                    WorkoutInfo workoutInfo = workoutUpdateObserver.getWorkoutInfo();
                    EventBus.getDefault().postSticky(workoutInfo);
                    IntentUtils.startActivity(this, StopActivity.class, null, Intent.FLAG_ACTIVITY_NEW_TASK);
                    break;
            }
        }
    }
}
