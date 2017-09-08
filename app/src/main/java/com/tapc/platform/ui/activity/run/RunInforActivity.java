package com.tapc.platform.ui.activity.run;

import com.tapc.platform.R;
import com.tapc.platform.ui.activity.BaseActivity;

/**
 * Created by Administrator on 2017/9/5.
 */

public class RunInforActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_run_infor;
    }

    @Override
    protected void initView() {
        mTapcApp.getService().setRunInforBarVisibility(false);
        mTapcApp.getService().setAppBarVisibility(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
