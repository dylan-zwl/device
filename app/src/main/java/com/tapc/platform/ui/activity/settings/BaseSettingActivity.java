package com.tapc.platform.ui.activity.settings;

import com.tapc.platform.ui.activity.BaseActivity;
import com.tapc.platform.ui.widget.SettingTopBar;

/**
 * Created by Administrator on 2017/10/10.
 */

public abstract class BaseSettingActivity extends BaseActivity {
    protected SettingTopBar mSettingTopBar;

    @Override
    protected void initView() {
        mSettingTopBar = new SettingTopBar(this);
        mSettingTopBar.show();
        super.initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSettingTopBar.dismiss();
        mSettingTopBar = null;
    }
}
