package com.tapc.platform.ui.activity.start;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.activity.MainActivity;
import com.tapc.platform.entity.ParameterSet;
import com.tapc.platform.entity.RunType;
import com.tapc.platform.library.data.TreadmillProgramSetting;
import com.tapc.platform.library.util.WorkoutEnum.ProgramType;
import com.tapc.platform.model.vaplayer.PlayEntity;
import com.tapc.platform.ui.activity.BaseActivity;
import com.tapc.platform.ui.fragment.mode.SelectModeFragment;
import com.tapc.platform.ui.fragment.parametersettings.ParameterSettingsFragment;
import com.tapc.platform.ui.fragment.program.ProgramStageFragment;
import com.tapc.platform.ui.view.TopTitle;
import com.tapc.platform.ui.widget.AlertDialog;
import com.tapc.platform.utils.FragmentUtils;
import com.tapc.platform.utils.IntentUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StartActivity extends BaseActivity {
    @BindView(R.id.start_top_title)
    TopTitle mTopTitle;

    private FragmentManager mManager;
    private ParameterSettingsFragment mParameterSettingsFragment;
    private RunType mRunType;
    private PlayEntity mPlayEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_start;
    }

    @Override
    protected void initView() {
        super.initView();
        mTapcApp.setHomeActivity(this.getClass());
//        mTapcApp.getService().getStartMenu().show();
        mManager = getFragmentManager();
        FragmentUtils.replaceFragment(this, mManager, R.id.start_mode_fragment, new SelectModeFragment(mListener));

        mTopTitle.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mParameterSettingsFragment != null) {
                    mParameterSettingsFragment = null;
                    mRunType = null;
                    mPlayEntity = null;
                    mTapcApp.setProgramSetting(null);
                    FragmentUtils.replaceFragment(mContext, mManager, R.id.start_mode_fragment, new SelectModeFragment
                            (mListener));
                } else {
                    IntentUtils.startActivity(mContext, MainActivity.class);
                    finish();
                }
            }
        });
    }

    public interface StartActivityListener {
        void switchParameterSettingsFragment(Context context, List<ParameterSet> list, RunType runType,
                                             ProgramType programType, Object... objects);

        void switchProgramStageFragment(Context context);
    }

    private StartActivityListener mListener = new StartActivityListener() {

        @Override
        public void switchParameterSettingsFragment(Context context, List<ParameterSet> list, @NonNull RunType runType,
                                                    ProgramType programType, Object... objects) {
            mParameterSettingsFragment = new ParameterSettingsFragment();
            mParameterSettingsFragment.init(programType);
            mRunType = runType;
            if (mRunType == RunType.VA) {
                if (objects != null && objects.length > 0) {
                    mPlayEntity = (PlayEntity) objects[0];
                }
            }
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
        if (mParameterSettingsFragment != null) {
            String overRangeStr = mParameterSettingsFragment.checkRange();
            if (!TextUtils.isEmpty(overRangeStr)) {
                new AlertDialog(mContext).setMsgText(overRangeStr).setButtonVisibility(View.GONE).setTimeOut(4000)
                        .show();
                return;
            }
            TreadmillProgramSetting programSetting = mParameterSettingsFragment.getProgramSetting();
            mTapcApp.setProgramSetting(programSetting);
        }

        mTapcApp.getService().getStartMenu().dismiss();
        Intent intent = new Intent();
        intent.setClass(mContext, CountdownActivity.class);
        intent.putExtra("run_type", mRunType);
        intent.putExtra("play_entity", mPlayEntity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mTapcApp.getService().getStartMenu().dismiss();
            IntentUtils.startActivity(mContext, MainActivity.class, null, Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                    .FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mManager = null;
    }
}
