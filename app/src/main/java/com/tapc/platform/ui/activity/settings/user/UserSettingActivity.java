package com.tapc.platform.ui.activity.settings.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.tapc.platform.R;
import com.tapc.platform.ui.activity.settings.BaseSettingActivity;
import com.tapc.platform.ui.widget.ShortcutKey;
import com.tapc.platform.utils.FragmentUtils;
import com.tapc.platform.utils.IntentUtils;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

/**
 * Created by Administrator on 2017/10/10.
 */

public class UserSettingActivity extends BaseSettingActivity {
    @BindView(R.id.settings_group)
    RadioGroup mRadioGroup;

    private ShortcutKey mShortcutKey;

    public static void lunch(Context context, String fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("fragment", fragment);
        IntentUtils.startActivity(context, UserSettingActivity.class, bundle, Intent.FLAG_ACTIVITY_NEW_TASK);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_user_setting;
    }

    @Override
    protected void initView() {
        super.initView();
        int rid = R.id.settings_language_btn;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String frament = bundle.getString("fragment");
            if (!TextUtils.isEmpty(frament)) {
                switch (frament) {
                    case "wifi":
                        rid = R.id.settings_net_btn;
                        break;
                    case "bluetooth":
                        rid = R.id.settings_bluetooth_btn;
                        break;
                }
            }
        }
        mRadioGroup.check(rid);
        mShortcutKey = mTapcApp.getService().getShortcutKey();
        if (mShortcutKey != null) {
            mShortcutKey.setFullScreen(true);
        } else {
            mSettingTopBar.setTitleClick(true);
        }

        //        FragmentUtils.replaceFragment(mContext, getFragmentManager(), R.id.settings_fl, WifiFragment.class);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mShortcutKey != null) {
            mShortcutKey.setFullScreen(false);
        }
    }
}
