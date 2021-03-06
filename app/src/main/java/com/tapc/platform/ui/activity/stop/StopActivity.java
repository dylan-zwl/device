package com.tapc.platform.ui.activity.stop;

import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tapc.platform.R;
import com.tapc.platform.activity.MainActivity;
import com.tapc.platform.entity.WorkoutInforItem;
import com.tapc.platform.entity.WorkoutInforType;
import com.tapc.platform.library.abstractset.WorkoutInfo;
import com.tapc.platform.library.data.TreadmillWorkoutInfo;
import com.tapc.platform.ui.activity.BaseActivity;
import com.tapc.platform.ui.adpater.WorkoutResultAdapter;
import com.tapc.platform.ui.view.RoundProgressBar;
import com.tapc.platform.ui.view.TopTitle;
import com.tapc.platform.utils.IntentUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StopActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.result_title)
    TopTitle mTitle;
    @BindView(R.id.result_round_pbar)
    RoundProgressBar mRoundProgressBar;

    private List<WorkoutInforItem> mDataList;
    private Handler mHandler;

    @Override
    protected int getContentView() {
        return R.layout.activity_stop;
    }

    @Override
    protected void initView() {
        super.initView();
        mTapcApp.getService().stopDevice();

        mTitle.setShowBack(true);
        mTapcApp.setProgramSetting(null);
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                back();
            }
        }, 30000);
        EventBus.getDefault().register(this);
    }

    private void initListView(TreadmillWorkoutInfo workoutInfo) {
        mDataList = new ArrayList<>();
        if (workoutInfo != null) {
            int time = workoutInfo.getTime();
            float distance = workoutInfo.getDistance();
            float calorie = workoutInfo.getCalorie();
            int heartRate = workoutInfo.getHeart();
            float speed = workoutInfo.getSpeed();

            initDataList(WorkoutInforType.DISTANCE, R.drawable.ic_result_distance, getString(R.string.distance),
                    String.format("%.2f", distance), getString(R.string.distance_unit));
            initDataList(WorkoutInforType.CALORIE, R.drawable.ic_result_calorie, getString(R.string.calorie), String
                    .format("%.2f", calorie), getString(R.string.calorie_unit));
            initDataList(WorkoutInforType.TIME, R.drawable.ic_result_time, getString(R.string.time), String.format
                    ("%02d:%02d:%02d", time / 3600, time % 3600 / 60, time % 60), "");
            initDataList(WorkoutInforType.HEART_RATE, R.drawable.ic_result_heart, getString(R.string.heart_rate),
                    String.valueOf(heartRate), getString(R.string.heart_rate_unit));
            initDataList(WorkoutInforType.SPEED, R.drawable.ic_result_speed, getString(R.string.speed), String.format
                    ("%.1f", speed), getString(R.string.speed_unit));


            int goal = (int) workoutInfo.getProgramType().getGoal() * 1000;
            int progress = 0;
            switch (workoutInfo.getProgramType().getWorkoutGoal()) {
                case TIME:
                    progress = time * 1000;
                    break;
                case CALORIE:
                    progress = (int) (calorie * 1000);
                    break;
                case DISTANCE:
                    progress = (int) (distance * 1000);
                    break;
            }
            setProgress(goal, progress);
        }

        WorkoutResultAdapter adpater = new WorkoutResultAdapter(mDataList);
        mRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 5));
        mRecyclerview.setAdapter(adpater);
        adpater.notifyDataSetChanged();
    }

    private void setProgress(int max, int progress) {
        mRoundProgressBar.setMax(max);
        mRoundProgressBar.setProgress(progress);
    }

    private void initDataList(WorkoutInforType type, int iconId, String name, String value, String unit) {
        WorkoutInforItem item = new WorkoutInforItem(type, iconId, name, value, unit);
        mDataList.add(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100, sticky = true)
    public void onResultEvent(WorkoutInfo workoutInfo) {
        initListView((TreadmillWorkoutInfo) workoutInfo);
    }

    @OnClick(R.id.title_back)
    void back() {
        IntentUtils.startActivity(mContext, MainActivity.class);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
    }
}
