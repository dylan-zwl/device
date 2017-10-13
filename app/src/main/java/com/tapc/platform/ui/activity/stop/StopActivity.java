package com.tapc.platform.ui.activity.stop;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tapc.platform.R;
import com.tapc.platform.entity.WorkoutInforItem;
import com.tapc.platform.entity.WorkoutInforType;
import com.tapc.platform.library.data.TreadmillWorkoutInfo;
import com.tapc.platform.library.workouting.WorkOuting;
import com.tapc.platform.ui.activity.BaseActivity;
import com.tapc.platform.ui.activity.MainActivity;
import com.tapc.platform.ui.adpater.WorkoutResultAdpater;
import com.tapc.platform.ui.view.RoundProgressBar;
import com.tapc.platform.utils.IntentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StopActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.title_back)
    Button mBackBtn;
    @BindView(R.id.result_round_pbar)
    RoundProgressBar mRoundProgressBar;

    List<WorkoutInforItem> mDataList;

    @Override
    protected int getContentView() {
        return R.layout.activity_stop;
    }

    @Override
    protected void initView() {
        super.initView();
        mBackBtn.setVisibility(View.VISIBLE);

        mTapcApp.setProgramSetting(null);

        mDataList = new ArrayList<WorkoutInforItem>();
        TreadmillWorkoutInfo workoutInfo = (TreadmillWorkoutInfo) WorkOuting.getInstance().getWorkoutInfo();
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

            mRoundProgressBar.setProgress(50);
        }
        WorkoutResultAdpater adpater = new WorkoutResultAdpater(mDataList);
        mRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 5));
        mRecyclerview.setAdapter(adpater);
        adpater.notifyDataSetChanged();
    }

    private void initDataList(WorkoutInforType type, int iconId, String name, String value, String unit) {
        WorkoutInforItem item = new WorkoutInforItem(type, iconId, name, value, unit);
        mDataList.add(item);
    }

    @OnClick(R.id.title_back)
    void back() {
        IntentUtils.startActivity(mContext, MainActivity.class);
        finish();
    }
}
