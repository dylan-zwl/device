package com.tapc.platform.ui.fragment.selectmode;


import android.app.Fragment;
import android.app.FragmentManager;
import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.ui.fragment.BaseFragment;

import butterknife.OnClick;

import static com.tapc.platform.ui.activity.start.StartActivity.replaceFragment;

/**
 * Created by Administrator on 2017/8/25.
 */

public class SelectModeFragment extends BaseFragment {
    private FragmentManager mManager;

    @Override
    protected int getContentView() {
        return R.layout.fragment_select_mode;
    }

    @Override
    protected void initView() {
        mManager = getChildFragmentManager();
        replaceFragment(R.id.mode_fragment, Fragment.instantiate(mContext, GoalModeFragment.class.getName()), mManager);
    }

    @OnClick({R.id.select_goal_mode, R.id.select_va_mode, R.id.select_program_mode, R.id.select_map_mode})
    void onSelectModeClick(View v) {
        String className = "";
        switch (v.getId()) {
            case R.id.select_goal_mode:
                className = GoalModeFragment.class.getName();
                break;
            case R.id.select_va_mode:
                className = VaFragment.class.getName();
                break;
            case R.id.select_program_mode:
                className = GoalModeFragment.class.getName();
                break;
            case R.id.select_map_mode:
                className = GoalModeFragment.class.getName();
                break;
        }
        replaceFragment(R.id.mode_fragment, Fragment.instantiate(mContext, className), mManager);
    }
}
