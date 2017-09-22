package com.tapc.platform.ui.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.tapc.platform.R;
import com.tapc.platform.entity.WorkoutInforItem;
import com.tapc.platform.entity.WorkoutInforType;
import com.tapc.platform.library.data.TreadmillWorkout;
import com.tapc.platform.library.util.WorkoutEnum.WorkoutUpdate;
import com.tapc.platform.library.workouting.WorkOuting;
import com.tapc.platform.ui.adpater.RunInforAdpater;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/28.
 */

public class RunInforBar extends BaseView implements Observer {
    @BindView(R.id.run_infor_lv)
    RecyclerView mRecyclerView;
    @BindView(R.id.run_infor_bg)
    LinearLayout mBg;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowManagerParams;
    private RunInforAdpater mRunInforAdpater;
    List<WorkoutInforItem> mDataList;
    private WorkOuting mWorkOuting;

    public RunInforBar(Context context, WindowManager windowManager, WindowManager.LayoutParams windowManagerParams) {
        super(context);
        mWindowManager = windowManager;
        mWindowManagerParams = windowManagerParams;
    }

    @Override
    protected int getContentView() {
        return R.layout.widget_run_infor;
    }

    @Override
    protected void initView() {
        super.initView();
        mDataList = new ArrayList<>();
        mDataList.add(new WorkoutInforItem(WorkoutInforType.DISTANCE, R.drawable.ic_bar_distance, "", "", getString
                (R.string.distance_unit)));
        mDataList.add(new WorkoutInforItem(WorkoutInforType.CALORIE, R.drawable.ic_bar_caloria, "", "", getString
                (R.string.calorie_unit)));
        mDataList.add(new WorkoutInforItem(WorkoutInforType.TIME, R.drawable.ic_bar_time, "", "", ""));
        mDataList.add(new WorkoutInforItem(WorkoutInforType.HEART_RATE, R.drawable.ic_bar_heart, "", "", getString(R
                .string.heart_rate_unit)));
        mRunInforAdpater = new RunInforAdpater(mDataList);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mRunInforAdpater);

        mWorkOuting = WorkOuting.getInstance();
        mWorkOuting.subscribeObserverNotification(this);
    }

    private String getString(int id) {
        return mContext.getString(id);
    }

//    @OnClick(R.id.run_infor_chx)
//    void runInforOnClick(View v) {
//        IntentUtils.startActivity(mContext, RunInforActivity.class, null, Intent.FLAG_ACTIVITY_NEW_TASK | Intent
//                .FLAG_ACTIVITY_CLEAR_TOP);
//    }

//    @OnCheckedChanged(R.id.run_infor_chx)
//    void runInforChx(CompoundButton buttonView, boolean isChecked) {
//        if (isChecked) {
//            mBg.setBackgroundResource(R.drawable.bg_run_infor_bar);
//            mRecyclerView.setVisibility(VISIBLE);
//        } else {
//            mBg.setBackground(null);
//            mRecyclerView.setVisibility(GONE);
//        }
//    }

    @Override
    public void update(Observable o, Object arg) {
        if (isShown()) {
            WorkoutUpdate workoutUpdate = (WorkoutUpdate) arg;
            if (workoutUpdate != null) {
                TreadmillWorkout workout = (TreadmillWorkout) mWorkOuting.getWorkout();
                if (workout == null || workoutUpdate != WorkoutUpdate.UPDATE) {
                    return;
                }
                for (WorkoutInforItem item : mDataList) {
                    switch (item.getType()) {
                        case DISTANCE:
                            item.setValue(String.format("%.2f", workout.getTotalDistance()));
                            break;
                        case CALORIE:
                            item.setValue(String.format("%.2f", workout.getTotalCalorie()));
                            break;
                        case TIME:
                            int time = workout.getTotalTime();
                            item.setValue(String.format("%02d:%02d:%02d", time / 3600, time % 3600 / 60, time % 60));
                            break;
                        case HEART_RATE:
                            item.setValue(String.valueOf(workout.getHeart()));
                            break;
                        case ALTITUDE:
                            item.setValue(String.format("%.2f", workout.getAltitude()));
                            break;
                        case PACE:
                            item.setValue(String.valueOf(workout.getPace()));
                            break;
                    }
                }
                mRunInforAdpater.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWorkOuting.unsubscribeObserverNotification(this);
    }
}
