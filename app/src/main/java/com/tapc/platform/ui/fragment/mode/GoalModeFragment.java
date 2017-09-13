package com.tapc.platform.ui.fragment.mode;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.ui.activity.start.StartActivity;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.ui.adpater.GoalModeAdpater;
import com.tapc.platform.entity.GoalModeItem;
import com.tapc.platform.ui.fragment.BaseFragment;
import com.tapc.platform.ui.fragment.parametersettings.ParameterSettingsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/25.
 */

public class GoalModeFragment extends BaseFragment {
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
        GoalModeAdpater goalModeAdpater = new GoalModeAdpater(list);
        mRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 5));
        mRecyclerview.setAdapter(goalModeAdpater);
        goalModeAdpater.notifyDataSetChanged();

        goalModeAdpater.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<GoalModeItem>() {
            @Override
            public void onItemClick(View view, GoalModeItem goalModeItem) {
                StartActivity.replaceFragment(mContext, ParameterSettingsFragment.class);
            }
        });
    }
}