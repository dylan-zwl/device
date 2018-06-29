package com.tapc.platform.ui.fragment.mode;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.entity.GoalModeItem;
import com.tapc.platform.entity.ParameterSet;
import com.tapc.platform.entity.RunType;
import com.tapc.platform.library.common.TreadmillSystemSettings;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.ui.adpater.GoalModeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/25.
 */

public class GoalModeFragment extends ModeBaseFragment {
    @BindView(R.id.mode_recyclerview)
    RecyclerView mRecyclerview;

    @Override
    protected int getContentView() {
        return R.layout.fragment_mode;
    }

    @Override
    protected void initView() {
        List<GoalModeItem> list = new ArrayList<GoalModeItem>();
        for (GoalModeItem item : GoalModeItem.values()) {
            list.add(item);
        }
        GoalModeAdapter goalModeAdapter = new GoalModeAdapter(list);
        mRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 5));
        mRecyclerview.setAdapter(goalModeAdapter);
        goalModeAdapter.notifyDataSetChanged();

        goalModeAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<GoalModeItem>() {
            @Override
            public void onItemClick(View view, GoalModeItem goalModeItem) {
                if (mListener != null) {
                    List<ParameterSet> list = new ArrayList<>();
                    switch (goalModeItem.getProgramType()) {
                        case TIME:
                            list.add(new ParameterSet(getString(R.string.time), "30", getString(R.string.min_unit),
                                    getDefaultValues(30, 60, 90), new ParameterSet.Range(5, 120)));
                            break;
                        case DISTANCE:
                            list.add(new ParameterSet(getString(R.string.distance), "5", getString(R.string
                                    .distance_unit), getDefaultValues(5, 10, 20), new ParameterSet.Range(0.2f, 200f)));
                            break;
                        case CALORIE:
                            list.add(new ParameterSet(getString(R.string.calorie), "100", getString(R.string
                                    .calorie_unit), getDefaultValues(100, 200, 300), new ParameterSet.Range(10, 2000)));
                            break;
                    }

                    if (goalModeItem.getName() == R.string.heart_rate) {
                        list.add(new ParameterSet(getString(R.string.heart_rate), "80", "", getDefaultValues(80, 90,
                                100), new ParameterSet.Range(50, 150)));
                        list.add(new ParameterSet(getString(R.string.age), "25", "", getDefaultValues(18, 25, 36),
                                new ParameterSet.Range(6, 100)));
                        list.add(new ParameterSet(getString(R.string.weight), "60", getString(R.string.weight_unit),
                                getDefaultValues(60, 80, 100), new ParameterSet.Range(20, 300)));
                    } else {
                        list.add(new ParameterSet(getString(R.string.speed), "1.0", getString(R.string.speed_unit),
                                getDefaultValues(3.0, 6.0, 9.0), new ParameterSet.Range(TreadmillSystemSettings
                                .MIN_SPEED, TreadmillSystemSettings.MAX_SPEED)));
                        list.add(new ParameterSet(getString(R.string.incline), "0.0", getString(R.string
                                .incline_unit), getDefaultValues(4.0, 8.0, 12.0), new ParameterSet.Range
                                (TreadmillSystemSettings.MIN_INCLINE, TreadmillSystemSettings.MAX_INCLINE)));
                    }
                    mListener.switchParameterSettingsFragment(mContext, list, RunType.NOMAL, goalModeItem
                            .getProgramType());
                }
            }
        });
    }
}
