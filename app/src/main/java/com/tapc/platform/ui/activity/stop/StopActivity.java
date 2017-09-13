package com.tapc.platform.ui.activity.stop;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tapc.platform.R;
import com.tapc.platform.entity.WorkoutResultItem;
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

    List<WorkoutResultItem> mDataList;

    @Override
    protected int getContentView() {
        return R.layout.activity_stop;
    }

    @Override
    protected void initView() {
        super.initView();
        mBackBtn.setVisibility(View.VISIBLE);

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

        mRoundProgressBar.setProgress(50);
    }

    private void initDataList(int iconId, String name, String value, String unit) {
        WorkoutResultItem item = new WorkoutResultItem(iconId, name, value, unit);
        mDataList.add(item);
    }

    @OnClick(R.id.title_back)
    void back() {
        IntentUtils.startActivity(mContext, MainActivity.class);
        finish();
    }
}
