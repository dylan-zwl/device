package com.tapc.platform.ui.activity.settings.user;

import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.tapc.platform.R;
import com.tapc.platform.ui.activity.settings.BaseSettingActivity;
import com.tapc.platform.utils.FragmentUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/10.
 */

public class UserSettingActivity extends BaseSettingActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.settings_language_btn)
    RadioButton mLanguageBtn;
    @BindView(R.id.settings_net_btn)
    RadioButton mNeteBtn;
    @BindView(R.id.settings_bluetooth_btn)
    RadioButton mBlutoothBtn;
    @BindView(R.id.settings_other_btn)
    RadioButton mOtherBtn;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_setting;
    }

    @Override
    protected void initView() {
        super.initView();
        mLanguageBtn.setOnCheckedChangeListener(this);
        mNeteBtn.setOnCheckedChangeListener(this);
        mBlutoothBtn.setOnCheckedChangeListener(this);
        mOtherBtn.setOnCheckedChangeListener(this);
        mLanguageBtn.setChecked(true);
//        FragmentUtils.replaceFragment(mContext, getFragmentManager(), R.id.settings_fl, WifiFragment.class);
//        FragmentUtils.replaceFragment(mContext, getFragmentManager(), R.id.settings_fl, LanguageFragment.class);
        FragmentUtils.replaceFragment(mContext, getFragmentManager(), R.id.settings_fl, UserDeviceFragment.class);
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
                case R.id.settings_other_btn:
                    cls = UserDeviceFragment.class;
                    break;
            }
//            FragmentUtils.replaceFragment(mContext, getFragmentManager(), R.id.settings_fl, cls);
        } else {
            buttonView.setTextColor(getResources().getColor(R.color.commonColor2));
        }
    }
}
