package com.tapc.platform.ui.fragment.mode;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.entity.GoalModeItem;
import com.tapc.platform.entity.ParameterSet;
import com.tapc.platform.entity.RunType;
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
                    List<ParameterSet> list = new ArrayList<ParameterSet>();
                    List<Object> defValues = new ArrayList<Object>();
                    defValues.add("10");
                    defValues.add("20");
                    defValues.add("30");
                    switch (goalModeItem.getProgramType()) {
                        case TIME:
                            list.add(new ParameterSet("时间", "30", "min", defValues));
                            break;
                        case DISTANCE:
                            list.add(new ParameterSet("距离", "5", "km", defValues));
                            break;
                        case CALORIE:
                            list.add(new ParameterSet("卡路里", "100", "kcal", defValues));
                            break;
                    }

                    if (goalModeItem.getName() == R.string.heart_rate) {
                        list.add(new ParameterSet("心率", "80", "", defValues));
                        list.add(new ParameterSet("年龄", "25", "kg", defValues));
                        list.add(new ParameterSet("体重", "60", "kg", defValues));
                    } else {
                        list.add(new ParameterSet("速度", "1.0", "km/h", defValues));
                        list.add(new ParameterSet("坡度", "0", "%", defValues));
                    }
                    mListener.switchParameterSettingsFragment(mContext, list, RunType.NOMAL, goalModeItem
                            .getProgramType());
                }
            }
        });
    }
}
