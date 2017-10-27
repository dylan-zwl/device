package com.tapc.platform.ui.activity;

import android.widget.ImageView;

import com.tapc.platform.R;
import com.tapc.platform.model.ConfigModel;
import com.tapc.platform.ui.activity.settings.user.UserSettingActivity;
import com.tapc.platform.ui.activity.start.StartActivity;
import com.tapc.platform.utils.IntentUtils;
import com.tapc.platform.utils.QrcodeUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.main_qr_iv)
    ImageView mQrIv;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
//        IntentUtils.startActivity(mContext, RunInforActivity.class);
        float a = getResources().getDisplayMetrics().density;
//        IntentUtils.startActivity(mContext, UserSettingActivity.class);
//        IntentUtils.startActivity(mContext, SystemSettingActivity.class);

        QrcodeUtils.show(ConfigModel.getDeviceId(mContext, ""), mQrIv, 10, bindUntilEvent(ActivityEvent.DESTROY));
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
    }

    @OnClick(R.id.main_start)
    void start() {
        IntentUtils.startActivity(mContext, StartActivity.class);
        finish();
    }

    @OnClick(R.id.main_start_setting)
    void startSetting() {
        IntentUtils.startActivity(mContext, UserSettingActivity.class);
    }
}
