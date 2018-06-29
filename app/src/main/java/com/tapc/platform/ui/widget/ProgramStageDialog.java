package com.tapc.platform.ui.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.tapc.platform.R;
import com.tapc.platform.db.IntervalEntity;
import com.tapc.platform.library.data.TreadmillWorkout;
import com.tapc.platform.library.data.TreadmillWorkoutInterval;
import com.tapc.platform.library.util.WorkoutEnum;
import com.tapc.platform.library.workouting.WorkOuting;
import com.tapc.platform.library.workouting.WorkoutUpdateObserver;
import com.tapc.platform.model.program.ProgramModel;
import com.tapc.platform.ui.view.BaseSystemView;
import com.tapc.platform.ui.view.RunProgramStage;
import com.tapc.platform.utils.WindowManagerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/13.
 */

public class ProgramStageDialog extends BaseSystemView implements Observer {
    @BindView(R.id.speed_stage)
    RunProgramStage mSpeedStage;
    @BindView(R.id.incline_stage)
    RunProgramStage mInclineStage;

    private WorkOuting mWorkOuting;
    private int mCurrentStage;
    private int mOldStage = -1;
    private List<IntervalEntity> mProgramList;

    public ProgramStageDialog(Context context) {
        super(context);
    }

    @Override
    public WindowManager.LayoutParams getLayoutParams() {
        return WindowManagerUtils.getLayoutParams(36, 132, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity
                .TOP | Gravity.LEFT);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_program_stage;
    }

    @Override
    protected void initView() {
        super.initView();
        mSpeedStage.setTitle(mContext.getString(R.string.speed));
        mInclineStage.setTitle(mContext.getString(R.string.incline));
        mWorkOuting = WorkOuting.getInstance();

        initProgramList();
    }

    private void initProgramList() {
        String programName = WorkoutEnum.ProgramType.TAPC_PROG.getName();
        ProgramModel programModel = new ProgramModel(mContext, "program.db", "TAPC_PROG");
        mProgramList = programModel.getProgram(programName);
        ArrayList<Float> speedList = new ArrayList<>();
        ArrayList<Float> inclineList = new ArrayList<>();
        for (IntervalEntity entity : mProgramList) {
            speedList.add(entity.getSpeed());
            inclineList.add(entity.getIncline());
        }
        mSpeedStage.setProgramChart(speedList, speedList);
        mInclineStage.setProgramChart(inclineList, inclineList);
    }

    @Override
    public void show() {
        super.show();
        mWorkOuting.subscribeObserverNotification(this);
    }

    @Override
    public void onDestroy() {
        mWorkOuting.unSubscribeObserverNotification(this);
    }


    @Override
    public void update(Observable o, Object arg) {
        WorkoutUpdateObserver workoutUpdateObserver = (WorkoutUpdateObserver) arg;
        if (workoutUpdateObserver != null) {
            TreadmillWorkout workout = (TreadmillWorkout) mWorkOuting.getWorkout();
            if (workout == null) {
                return;
            }
            switch (workoutUpdateObserver.getWorkoutUpdate()) {
                case UI_UPDATE:
                    float speed = workout.getSpeed();
                    float incline = workout.getIncline();

                    TreadmillWorkoutInterval interval = (TreadmillWorkoutInterval) workoutUpdateObserver
                            .getWorkoutInterval();
                    if (interval != null) {
                        if (interval.getIntervalList() != null) {
                            mCurrentStage = interval.getIntervalList().size();
                            if (mCurrentStage != mOldStage) {
                                mOldStage = mCurrentStage;
                                mSpeedStage.setProgramValue(mProgramList.get(mCurrentStage).getSpeed());
                                mInclineStage.setProgramValue(mProgramList.get(mCurrentStage).getIncline());
                            }
                        }
                    }
                    mSpeedStage.setCurrentValue(speed);
                    mInclineStage.setCurrentValue(incline);
                    break;
            }
        }
    }
}
