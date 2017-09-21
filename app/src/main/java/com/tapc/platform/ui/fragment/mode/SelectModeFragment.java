package com.tapc.platform.ui.fragment.mode;


import android.app.Fragment;
import android.app.FragmentManager;
import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.ui.activity.start.StartActivity;
import com.tapc.platform.ui.fragment.BaseFragment;
import com.tapc.platform.utils.FragmentUtils;

import butterknife.OnClick;


/**
 * Created by Administrator on 2017/8/25.
 */

public class SelectModeFragment extends BaseFragment {
    private FragmentManager mManager;
    private StartActivity.StartActivityListener mListener;

    public SelectModeFragment(StartActivity.StartActivityListener listener) {
        super();
        mListener = listener;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_select_mode;
    }

    @Override
    protected void initView() {
        mManager = getChildFragmentManager();
        replaceFragment(GoalModeFragment.class);
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
        replaceFragment(cls);
    }

    private void replaceFragment(Class<?> cls) {
        if (cls != null) {
            ModeBaseFragment modeBaseFragment = (ModeBaseFragment) Fragment.instantiate(mContext, cls.getName());
            if (modeBaseFragment != null) {
                modeBaseFragment.setListener(mListener);
                FragmentUtils.replaceFragment(mContext, mManager, R.id.mode_fragment, modeBaseFragment);
            }
        }
    }
}
