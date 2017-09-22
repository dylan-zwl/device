package com.tapc.platform.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.application.TapcApplication;
import com.tapc.platform.entity.WidgetShowStatus;
import com.tapc.platform.library.common.AppSettings;
import com.tapc.platform.library.common.CommonEnum;
import com.tapc.platform.library.common.TreadmillListener;
import com.tapc.platform.library.common.TreadmillSystemSettings;
import com.tapc.platform.library.controller.MachineController;
import com.tapc.platform.library.data.TreadmillProgramSetting;
import com.tapc.platform.library.data.TreadmillWorkout;
import com.tapc.platform.library.util.WorkoutEnum.ProgramType;
import com.tapc.platform.library.util.WorkoutEnum.WorkoutUpdate;
import com.tapc.platform.library.workouting.WorkOuting;
import com.tapc.platform.ui.activity.stop.StopActivity;
import com.tapc.platform.ui.view.DeviceCtl;
import com.tapc.platform.ui.view.FastSetDeviceCtl;
import com.tapc.platform.utils.AppUtils;
import com.tapc.platform.utils.FormatUtils;
import com.tapc.platform.utils.IntentUtils;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/8/28.
 */

public class BottomBar extends BaseView implements Observer, TreadmillListener {
    @BindView(R.id.bottombar_left_ctl)
    DeviceCtl mLeftDeviceCtl;
    @BindView(R.id.bottombar_right_ctl)
    DeviceCtl mRightDeviceCtl;
    @BindView(R.id.bottombar_ctl_ll)
    LinearLayout mCtlLL;
    @BindView(R.id.bottombar_fast_set_ctl)
    FastSetDeviceCtl mFastSetDeviceCtl;
    @BindView(R.id.bottombar_time)
    TextView mTimeTv;
    @BindView(R.id.bottombar_time_progrress)
    ProgressBar mTimePbar;

    @BindView(R.id.bottombar_resume)
    Button mResumeBtn;
    @BindView(R.id.bottombar_pause)
    Button mPauseBtn;

    private WorkOuting mWorkOuting;

    @Override
    protected int getContentView() {
        return R.layout.widget_bottom_bar;
    }

    public BottomBar(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        super.initView();

        initWortout();

        mLeftDeviceCtl.setConfig(TreadmillSystemSettings.MIN_INCLINE, TreadmillSystemSettings.MAX_INCLINE,
                TreadmillSystemSettings.STEP_INCLINE, 0.0f);
        mLeftDeviceCtl.setOnClickListener(new DeviceCtl.Listener() {
            @Override
            public void onAddClick() {
                mLeftDeviceCtl.setValue(mWorkOuting.onLeftKeypadAdd());
            }

            @Override
            public void onSubClick() {
                mLeftDeviceCtl.setValue(mWorkOuting.onLeftKeypadSub());
            }

            @Override
            public void setDeviceValue(float value) {
                mWorkOuting.onLeftPanel(value);
            }

            @Override
            public void onCtlTypeClick(int icon) {
                updateFastSetList((int) mLeftDeviceCtl.getMinValue(), (int) mLeftDeviceCtl.getMaxValue());
                setFastSetDeviceCtlType(icon);
                setFastSetCtlVisibility(true);
            }
        });

        mRightDeviceCtl.setConfig(TreadmillSystemSettings.MIN_SPEED, TreadmillSystemSettings.MAX_SPEED,
                TreadmillSystemSettings.STEP_SPEED, 0.0f);
        mRightDeviceCtl.setOnClickListener(new DeviceCtl.Listener() {
            @Override
            public void onAddClick() {
                mRightDeviceCtl.setValue(mWorkOuting.onRightKeypadAdd());
            }

            @Override
            public void onSubClick() {
                mRightDeviceCtl.setValue(mWorkOuting.onRightKeypadSub());
            }

            @Override
            public void setDeviceValue(float value) {
                mWorkOuting.onRightPanel(value);
            }

            @Override
            public void onCtlTypeClick(int icon) {
                int min = (int) FormatUtils.formatFloat(0, mRightDeviceCtl.getMinValue(), RoundingMode.UP);
                updateFastSetList(min, (int) mRightDeviceCtl.getMaxValue());
                setFastSetDeviceCtlType(icon);
                setFastSetCtlVisibility(true);
            }
        });
        mFastSetDeviceCtl.setListener(new FastSetDeviceCtl.Listener() {
            @Override
            public void onValueClick(String value) {
                if (mFastSetDeviceCtl.getIconId() == mLeftDeviceCtl.getIcon()) {
                    mWorkOuting.onLeftPanel(Float.valueOf(value));
                } else if (mFastSetDeviceCtl.getIconId() == mRightDeviceCtl.getIcon()) {
                    mWorkOuting.onRightPanel(Float.valueOf(value));
                }
                Log.d("set value", "value");
            }

            @Override
            public void close() {
                setFastSetCtlVisibility(false);
            }
        });

        TreadmillWorkout workout = (TreadmillWorkout) mWorkOuting.getWorkout();
        if (workout != null) {
            mLeftDeviceCtl.setValue(workout.getIncline());
            mRightDeviceCtl.setValue(workout.getSpeed());
            setPauseBtnShow(!mWorkOuting.getIsPausing());
        }
    }

    private void initWortout() {
        mWorkOuting = WorkOuting.getInstance();
        if (mWorkOuting.getIsRunning() == false) {
            TreadmillProgramSetting programSetting = (TreadmillProgramSetting) TapcApplication.getInstance()
                    .getProgramSetting();
            if (programSetting == null) {
                ProgramType programType = ProgramType.NORMAL;
                programType.setGoal(120 * 60);
                TreadmillProgramSetting treadmillProgramSetting = new TreadmillProgramSetting(programType);
                treadmillProgramSetting.setSpeed(TreadmillSystemSettings.MIN_SPEED);
                treadmillProgramSetting.setIncline(TreadmillSystemSettings.MIN_INCLINE);
                programSetting = treadmillProgramSetting;
            }
            mWorkOuting = WorkOuting.getInstance();
            mWorkOuting.initWorkOuting(MachineController.getInstance().getMachineOperateListener(), mContext);
            mWorkOuting.setProgramSetting(programSetting);
            mWorkOuting.start();
        }
        MachineController.getInstance().setMachineListener(this);
        mWorkOuting.subscribeObserverNotification(this);
    }


    private void updateFastSetList(int min, int max) {
        List<String> list = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            list.add(String.valueOf(i));
        }
        mFastSetDeviceCtl.updateShow(list);
    }

    private void setFastSetDeviceCtlType(int icon) {
        mFastSetDeviceCtl.setIcon(icon);
    }

    private void setFastSetCtlVisibility(boolean visibility) {
        if (visibility) {
            mFastSetDeviceCtl.setVisibility(VISIBLE);
            mCtlLL.setVisibility(INVISIBLE);
            mFastSetDeviceCtl.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_right_in));
            mCtlLL.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_left_out));
        } else {
            mFastSetDeviceCtl.setVisibility(GONE);
            mCtlLL.setVisibility(VISIBLE);
            mFastSetDeviceCtl.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_right_out));
            mCtlLL.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_left_in));
        }
    }

    private void setPauseBtnShow(boolean visibility) {
        if (visibility) {
            mResumeBtn.setVisibility(GONE);
            mPauseBtn.setVisibility(VISIBLE);
            mLeftDeviceCtl.setResume();
            mRightDeviceCtl.setResume();
        } else {
            mResumeBtn.setVisibility(VISIBLE);
            mPauseBtn.setVisibility(GONE);
            mLeftDeviceCtl.setPause();
            mRightDeviceCtl.setPause();
        }
    }

    @OnClick(R.id.bottombar_pause)
    void pause() {
        setPauseBtnShow(false);
        mWorkOuting.pause();
    }

    @OnClick(R.id.bottombar_resume)
    void resume() {
        setPauseBtnShow(true);
        mWorkOuting.resume();
    }

    @OnClick(R.id.bottombar_stop)
    void stop() {
        IntentUtils.startActivity(mContext, StopActivity.class, null, Intent.FLAG_ACTIVITY_NEW_TASK);
        TapcApplication.getInstance().getService().setAppBarVisibility(WidgetShowStatus.REMOVE);
        TapcApplication.getInstance().getService().setRunInforBarVisibility(WidgetShowStatus.REMOVE);
        TapcApplication.getInstance().getService().setProgramStageDialogVisibility(WidgetShowStatus.REMOVE);
        TapcApplication.getInstance().getService().setShortcutKeyVisibility(WidgetShowStatus.REMOVE);
        TapcApplication.getInstance().getService().setBottomBarVisibility(WidgetShowStatus.REMOVE);
        mWorkOuting.stop();
    }

    @Override
    public void onDestroy() {
        if (mWorkOuting != null) {
            mWorkOuting.unsubscribeObserverNotification(this);
        }
        mLeftDeviceCtl.cancelObserable();
        mRightDeviceCtl.cancelObserable();
    }

    @Override
    public void update(Observable o, Object arg) {
        WorkoutUpdate workoutUpdate = (WorkoutUpdate) arg;
        if (workoutUpdate != null) {
            TreadmillWorkout workout = (TreadmillWorkout) mWorkOuting.getWorkout();
            if (workout == null) {
                return;
            }
            switch (workoutUpdate) {
                case UPDATE:
                    switch (workout.getWorkoutGoal()) {
                        case TIME:
                            int time = workout.getTotalTime();
                            int goalTime = (int) workout.getGoal();
                            mTimePbar.setProgress(time / goalTime);
                            mTimeTv.setText(String.format("%02d:%02d:%02d", time / 3600, time % 3600 / 60, time % 60));
                            break;
                        case DISTANCE:
                            float distance = workout.getTotalDistance();
                            float goalDistance = workout.getGoal();
                            mTimePbar.setProgress((int) (distance / goalDistance));
                            mTimeTv.setText(String.format("%.02d", distance));
                            break;
                        case CALORIE:
                            float calorie = workout.getTotalCalorie();
                            float goalCalorie = workout.getGoal();
                            mTimePbar.setProgress((int) (calorie / goalCalorie));
                            mTimeTv.setText(String.format("%.02d", calorie));
                            break;
                    }
                    break;
                case LEFT:
                    mLeftDeviceCtl.setValue(workout.getIncline());
                    break;
                case RIGHT:
                    mRightDeviceCtl.setValue(workout.getSpeed());
                    break;
                case FINISH:
                    stop();
                    break;
            }
        }
    }

    @Override
    public void onHeartChanged(int i) {

    }

    @Override
    public void onRPMChanged(int i) {

    }

    @Override
    public void onPaceChanged(int i) {

    }

    @OnClick(R.id.bottombar_back)
    void backOnClick() {
        TapcApplication.getInstance().getKeyEvent().backEvent();
    }

    @OnClick(R.id.bottombar_home)
    void homeOnClick() {
        try {
            if (AppUtils.isApplicationBroughtToBackground(mContext) || AppSettings.getPlatform() == CommonEnum
                    .Platform.S700) {
                IntentUtils.startActivity(mContext, TapcApplication.getInstance().getHomeActivity(), null, Intent
                        .FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
//            IntentUtils.home(mContext);
        } catch (Exception e) {
            Log.d(this.toString(), e.getMessage());
        }
    }
}
