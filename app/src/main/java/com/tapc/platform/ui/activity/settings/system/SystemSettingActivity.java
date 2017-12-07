package com.tapc.platform.ui.activity.settings.system;

import android.content.Intent;
import android.provider.Settings;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.tapc.platform.R;
import com.tapc.platform.ui.activity.settings.BaseSettingActivity;
import com.tapc.platform.utils.FragmentUtils;

import butterknife.OnCheckedChanged;

/**
 * Created by Administrator on 2017/10/10.
 */

public class SystemSettingActivity extends BaseSettingActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_system_setting;
    }

    @Override
    protected void initView() {
        super.initView();
        ((RadioButton) findViewById(R.id.settings_device_infor_btn)).setChecked(true);
    }

    @OnCheckedChanged({R.id.settings_device_infor_btn, R.id.settings_parameter_btn, R.id.settings_install_btn, R.id
            .settings_uninstall_btn, R.id.settings_other_btn, R.id.settings_system_btn, R.id.settings_file_btn})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            buttonView.setTextColor(getResources().getColor(R.color.commonColor1));
            Class<?> cls = null;
            switch (buttonView.getId()) {
                case R.id.settings_device_infor_btn:
                    cls = DevInforFragment.class;
                    break;
                case R.id.settings_parameter_btn:
                    cls = CtlParameterFragment.class;
                    break;
                case R.id.settings_install_btn:
                    cls = InstallFragment.class;
                    break;
                case R.id.settings_uninstall_btn:
                    cls = UninstallFragment.class;
                    break;
                case R.id.settings_other_btn:
                    cls = OtherFragment.class;
                    break;
                case R.id.settings_system_btn:
                    this.startActivity(new Intent(Settings.ACTION_SETTINGS));
                    break;
                case R.id.settings_file_btn:
                    this.startActivity(new Intent(Settings.ACTION_SETTINGS));
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
