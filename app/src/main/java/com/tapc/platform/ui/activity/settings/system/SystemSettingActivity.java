package com.tapc.platform.ui.activity.settings.system;

import android.widget.CompoundButton;

import com.tapc.platform.R;
import com.tapc.platform.ui.activity.settings.BaseSettingActivity;
import com.tapc.platform.ui.activity.settings.user.BluetoothFragment;
import com.tapc.platform.ui.activity.settings.user.LanguageFragment;
import com.tapc.platform.ui.activity.settings.user.UserDeviceFragment;
import com.tapc.platform.ui.activity.settings.user.WifiFragment;
import com.tapc.platform.utils.FragmentUtils;

/**
 * Created by Administrator on 2017/10/10.
 */

public class SystemSettingActivity extends BaseSettingActivity implements CompoundButton.OnCheckedChangeListener {
    @Override
    protected int getContentView() {
        return R.layout.activity_system_setting;
    }

    @Override
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
            FragmentUtils.replaceFragment(mContext, getFragmentManager(), R.id.settings_fl, cls);
        } else {
            buttonView.setTextColor(getResources().getColor(R.color.commonColor2));
        }
    }
}
