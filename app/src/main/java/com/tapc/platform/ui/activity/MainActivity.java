package com.tapc.platform.ui.activity;

import com.tapc.platform.R;
import com.tapc.platform.ui.activity.start.StartActivity;
import com.tapc.platform.utils.IntentUtils;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
//        IntentUtils.startActivity(mContext, RunInforActivity.class);
        System.gc();
        float a = getResources().getDisplayMetrics().density;
    }

    @OnClick(R.id.main_start)
    void start() {
        IntentUtils.startActivity(mContext, StartActivity.class);
        finish();
    }
}
