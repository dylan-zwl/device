package com.tapc.platform.ui.activity.start;

import android.app.FragmentManager;
import android.content.Context;
import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.entity.WidgetShowStatus;
import com.tapc.platform.ui.activity.BaseActivity;
import com.tapc.platform.ui.fragment.mode.SelectModeFragment;
import com.tapc.platform.utils.FragmentUtils;
import com.tapc.platform.utils.IntentUtils;

import butterknife.OnClick;

public class StartActivity extends BaseActivity {
    private static FragmentManager sManager;

    @Override
    protected int getContentView() {
        return R.layout.activity_start;
    }

    @Override
    protected void initView() {
        super.initView();
        mTapcApp.setHomeActivity(this.getClass());
        mTapcApp.getService().setStartMenuVisibility(WidgetShowStatus.VISIBLE);
        sManager = getFragmentManager();
        FragmentUtils.replaceFragment(this, sManager, R.id.start_mode_fragment, SelectModeFragment.class);
//        FragmentUtils.replaceFragment(this, sManager, R.id.start_mode_fragment, ProgramStageFragment.class);
    }

    public static void replaceFragment(Context context, Class<?> cls) {
        FragmentUtils.replaceFragment(context, sManager, R.id.start_mode_fragment, cls);
    }

    @OnClick(R.id.start)
    void onStartClick(View v) {
        mTapcApp.getService().setStartMenuVisibility(WidgetShowStatus.REMOVE);
        IntentUtils.startActivity(mContext, CountdownActivity.class);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sManager = null;
    }
}
