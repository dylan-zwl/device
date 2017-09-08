package com.tapc.platform.ui.fragment.selectmode;


import android.app.FragmentManager;
import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.ui.fragment.BaseFragment;
import com.tapc.platform.utils.FragmentUtils;

import butterknife.OnClick;


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
        FragmentUtils.replaceFragment(mContext, mManager, R.id.mode_fragment, ProgramFragment.class);
    }

    @OnClick({R.id.select_goal_mode, R.id.select_va_mode, R.id.select_program_mode, R.id.select_map_mode})
    void onSelectModeClick(View v) {
        Class<?> cls = null;
        switch (v.getId()) {
            case R.id.select_goal_mode:
                cls = GoalModeFragment.class;
                break;
            case R.id.select_va_mode:
                cls = VaFragment.class;
                break;
            case R.id.select_program_mode:
                cls = ProgramFragment.class;
                break;
            case R.id.select_map_mode:
                cls = GoalModeFragment.class;
                break;
        }
        if (cls != null) {
            FragmentUtils.replaceFragment(mContext, mManager, R.id.mode_fragment, cls);
        }
    }
}
