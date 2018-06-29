package com.tapc.platform.ui.activity.run;

import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.entity.event.RunInforActivityExit;
import com.tapc.platform.library.data.TreadmillWorkout;
import com.tapc.platform.library.workouting.WorkOuting;
import com.tapc.platform.library.workouting.WorkoutUpdateObserver;
import com.tapc.platform.ui.view.RunInforView;
import com.tapc.platform.ui.view.TopTitle;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/5.
 */

public class RunInforActivity extends RunBaseActivity {
    @BindView(R.id.run_infor_title)
    TopTitle mTopTitle;

    @BindView(R.id.run_infor_used_time)
    RunInforView mUsedTime;
    @BindView(R.id.run_infor_remain_time)
    RunInforView mRemainTime;
    @BindView(R.id.run_infor_total_time)
    RunInforView mTotalTime;

    @BindView(R.id.run_infor_now_speed)
    RunInforView mCurrentSpeed;
    @BindView(R.id.run_infor_average_speed)
    RunInforView mAverageSpeed;
    @BindView(R.id.run_infor_highest_speed)
    RunInforView mHighestSpeed;

    @BindView(R.id.run_infor_now_incline)
    RunInforView mCurrentIncline;
    @BindView(R.id.run_infor_average_incline)
    RunInforView mAverageIncline;

    @BindView(R.id.run_infor_now_calorie)
    RunInforView mCurrentCalorie;
    @BindView(R.id.run_infor_total_calorie)
    RunInforView mTotalCalorie;

    @BindView(R.id.run_infor_now_heart)
    RunInforView mCurrentHeart;
    @BindView(R.id.run_infor_average_heart)
    RunInforView mAverageHeart;

    @BindView(R.id.run_infor_now_distance)
    RunInforView mCurrentDistance;
    @BindView(R.id.run_infor_total_distance)
    RunInforView mTotalDistance;

    @BindView(R.id.run_infor_total_altitude)
    RunInforView mTotalAltitude;

    @Override
    protected int getContentView() {
        return R.layout.activity_run_infor;
    }

    @Override
    protected void initView() {
        mTopTitle.setShowBack(true);
        mTopTitle.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new RunInforActivityExit());
            }
        });
        WorkOuting.getInstance().subscribeObserverNotification(this);
    }

    @Override
    protected void update(WorkoutUpdateObserver workoutUpdate) {
        switch (workoutUpdate.getWorkoutUpdate()) {
            case UI_UPDATE:
                TreadmillWorkout workout = (TreadmillWorkout) workoutUpdate.getWorkout();
                if (workout != null) {
                    int curTime = workout.getCurTime();
                    int remainTime = workout.getRemainTime();
                    int totalTime = curTime + remainTime;
                    mUsedTime.setValue(String.format("%02d:%02d:%02d", curTime / 3600, curTime % 3600 / 60, curTime %
                            60));
                    mRemainTime.setValue(String.format("%02d:%02d:%02d", remainTime / 3600, remainTime % 3600 / 60,
                            remainTime % 60));
                    mTotalTime.setValue(String.format("%02d:%02d:%02d", totalTime / 3600, totalTime % 3600 / 60,
                            totalTime % 60));

                    mCurrentSpeed.setValue(String.format("%.1f", workout.getSpeed()));
                    mAverageSpeed.setValue(String.format("%.1f", workout.getSpeedAvg()));
                    mHighestSpeed.setValue(String.format("%.1f", workout.getSpeedMax()));

                    mCurrentIncline.setValue(String.format("%.1f", workout.getIncline()));
                    mAverageIncline.setValue(String.format("%.1f", workout.getInclineAvg()));

                    mCurrentHeart.setValue("" + workout.getHeart());
                    mAverageHeart.setValue("" + workout.getHeartAvg());

                    mCurrentCalorie.setValue(String.format("%.2f", workout.getIncline()));
                    mTotalCalorie.setValue(String.format("%.2f", workout.getTotalCalorie()));

                    mCurrentDistance.setValue(String.format("%.2f", workout.getDistance()));
                    mTotalDistance.setValue(String.format("%.2f", workout.getTotalDistance()));

                    mTotalAltitude.setValue(String.format("%.2f", workout.getTotalAltitude()));
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        WorkOuting.getInstance().unSubscribeObserverNotification(this);
        finish();
    }
}
