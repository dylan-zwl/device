package com.tapc.platform.ui.activity.start;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.entity.ParameterSet;
import com.tapc.platform.entity.WidgetShowStatus;
import com.tapc.platform.library.data.TreadmillProgramSetting;
import com.tapc.platform.ui.activity.BaseActivity;
import com.tapc.platform.ui.fragment.mode.SelectModeFragment;
import com.tapc.platform.ui.fragment.parametersettings.ParameterSettingsFragment;
import com.tapc.platform.ui.fragment.program.ProgramStageFragment;
import com.tapc.platform.utils.FragmentUtils;

import java.util.List;

import butterknife.OnClick;

public class StartActivity extends BaseActivity {
    private FragmentManager mManager;
    private ParameterSettingsFragment mParameterSettingsFragment;

    @Override
    protected int getContentView() {
        return R.layout.activity_start;
    }

    @Override
    protected void initView() {
        super.initView();
        mTapcApp.setHomeActivity(this.getClass());
        mTapcApp.getService().setStartMenuVisibility(WidgetShowStatus.VISIBLE);
        mManager = getFragmentManager();
        FragmentUtils.replaceFragment(this, mManager, R.id.start_mode_fragment, new SelectModeFragment(mListener));
//        FragmentUtils.replaceFragment(this, mManager, R.id.start_mode_fragment, ProgramStageFragment.class);
    }

    public interface StartActivityListener {
        void switchParameterSettingsFragment(Context context, List<ParameterSet> list, Object... objects);

        void switchProgramStageFragment(Context context);
    }

    private StartActivityListener mListener = new StartActivityListener() {

        @Override
        public void switchParameterSettingsFragment(Context context, List<ParameterSet> list, Object... objects) {
            mParameterSettingsFragment = new ParameterSettingsFragment();
            mParameterSettingsFragment.init(objects);
            mParameterSettingsFragment.setDataList(list);
            FragmentUtils.replaceFragment(context, mManager, R.id.start_mode_fragment, mParameterSettingsFragment);
        }

        @Override
        public void switchProgramStageFragment(Context context) {
            FragmentUtils.replaceFragment(context, mManager, R.id.start_mode_fragment, ProgramStageFragment.class);
        }
    };

    @OnClick(R.id.start)
    void onStartClick(View v) {
        mTapcApp.getService().setStartMenuVisibility(WidgetShowStatus.REMOVE);
        Intent intent = new Intent();
        if (mParameterSettingsFragment != null) {
            TreadmillProgramSetting programSetting = mParameterSettingsFragment.getmProgramSetting();
//            intent.putExtra("program_setting", programSetting);
            mTapcApp.setProgramSetting(programSetting);
        }
        intent.setClass(mContext, CountdownActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mManager = null;
    }
}
