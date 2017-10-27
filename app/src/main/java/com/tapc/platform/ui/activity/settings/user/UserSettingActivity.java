package com.tapc.platform.ui.activity.settings.user;

import android.widget.CompoundButton;

import com.tapc.platform.R;
import com.tapc.platform.ui.activity.settings.BaseSettingActivity;
import com.tapc.platform.utils.FragmentUtils;

import butterknife.OnCheckedChanged;

/**
 * Created by Administrator on 2017/10/10.
 */

public class UserSettingActivity extends BaseSettingActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_user_setting;
    }

    @Override
    protected void initView() {
        super.initView();
//        FragmentUtils.replaceFragment(mContext, getFragmentManager(), R.id.settings_fl, WifiFragment.class);
//        FragmentUtils.replaceFragment(mContext, getFragmentManager(), R.id.settings_fl, BluetoothFragment.class);
//        FragmentUtils.replaceFragment(mContext, getFragmentManager(), R.id.settings_fl, LanguageFragment.class);
        FragmentUtils.replaceFragment(mContext, getFragmentManager(), R.id.settings_fl, UserDeviceFragment.class);
        mSettingTopBar.setTitleClick(true);
    }

    @OnCheckedChanged({R.id.settings_language_btn, R.id.settings_net_btn, R.id.settings_bluetooth_btn, R.id
            .settings_device_btn})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            buttonView.setTextColor(getResources().getColor(R.color.commonColor1));
            Class<?> cls = null;
            switch (buttonView.getId()) {
                case R.id.settings_language_btn:
                    cls = LanguageFragment.class;
                    break;
                case R.id.settings_net_btn:
                    cls = WifiFragment.class;
                    break;
                case R.id.settings_bluetooth_btn:
                    cls = BluetoothFragment.class;
                    break;
                case R.id.settings_device_btn:
                    cls = UserDeviceFragment.class;
                    break;
            }
            if (cls != null) {
                FragmentUtils.replaceFragment(mContext, getFragmentManager(), R.id.settings_fl, cls);
            }
        } else {
            buttonView.setTextColor(getResources().getColor(R.color.commonColor2));
        }
    }
}
