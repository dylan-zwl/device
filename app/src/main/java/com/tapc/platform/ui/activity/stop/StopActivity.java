package com.tapc.platform.ui.activity.stop;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tapc.platform.R;
import com.tapc.platform.ui.activity.BaseActivity;
import com.tapc.platform.ui.adpater.WorkoutResultAdpater;
import com.tapc.platform.entity.WorkoutResultItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class StopActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    List<WorkoutResultItem> mDataList;

    @Override
    protected int getContentView() {
        return R.layout.activity_stop;
    }

    @Override
    protected void initView() {
        super.initView();
        mDataList = new ArrayList<WorkoutResultItem>();
        initDataList(R.drawable.ic_result_time, "时间", "0", "");
        initDataList(R.drawable.ic_result_distance, "距离", "0", "km");
        initDataList(R.drawable.ic_result_calorie, "卡路里", "0", "kcal");
        initDataList(R.drawable.ic_result_heart, "心率", "0", "kpm");
        initDataList(R.drawable.ic_result_speed, "平均速度", "0", "km/h");

        WorkoutResultAdpater adpater = new WorkoutResultAdpater(mDataList);
        mRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 5));
        mRecyclerview.setAdapter(adpater);
        adpater.notifyDataSetChanged();
    }

    private void initDataList(int iconId, String name, String value, String unit) {
        WorkoutResultItem item = new WorkoutResultItem(iconId, name, value, unit);
        mDataList.add(item);
    }
}
