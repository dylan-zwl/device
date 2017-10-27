package com.tapc.platform.ui.activity.run;

import com.tapc.platform.library.util.WorkoutEnum.WorkoutUpdate;
import com.tapc.platform.library.workouting.WorkOuting;
import com.tapc.platform.ui.activity.BaseActivity;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/9/22.
 */

public class RunBaseActivity extends BaseActivity implements Observer {
    @Override
    protected int getContentView() {
        return 0;
    }

    @Override
    protected void initView() {
        super.initView();
        WorkOuting.getInstance().subscribeObserverNotification(this);
//        mTapcApp.getService().setRunInforBarVisibility(WidgetShowStatus.VISIBLE);
//        mTapcApp.getService().setProgramStageDialogVisibility(WidgetShowStatus.VISIBLE);

//        mTapcApp.getService().setAppBarVisibility(WidgetShowStatus.VISIBLE);
//        mTapcApp.getService().setShortcutKeyVisibility(WidgetShowStatus.VISIBLE);
//        mTapcApp.getService().setBottomBarVisibility(WidgetShowStatus.VISIBLE);
        mTapcApp.getService().startDevice();
    }

    @Override
    public void update(Observable o, Object arg) {
        WorkoutUpdate workoutUpdate = (WorkoutUpdate) arg;
        if (workoutUpdate != null) {
            update(workoutUpdate);
        }
    }

    protected void update(WorkoutUpdate workoutUpdate) {
        switch (workoutUpdate) {
            case UI_STOP:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WorkOuting.getInstance().unsubscribeObserverNotification(this);
    }
}
