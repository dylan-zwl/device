package com.tapc.platform.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.widget.ImageView;

import com.tapc.platform.R;
import com.tapc.platform.model.common.ConfigModel;
import com.tapc.platform.ui.activity.login.FaceDetectExpActivity;
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

    private Handler mHandler;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
//        IntentUtils.startActivity(mContext, RunInforActivity.class);
//        IntentUtils.startActivity(mContext, UserSettingActivity.class);
//        IntentUtils.startActivity(mContext, SystemSettingActivity.class);
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                QrcodeUtils.show(ConfigModel.getDeviceId(mContext, ""), mQrIv, 10, bindUntilEvent(ActivityEvent.STOP));
            }
        }, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
    }

    @OnClick(R.id.main_start)
    void start() {
        IntentUtils.startActivity(mContext, StartActivity.class, null, Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    }

    @OnClick(R.id.main_start_setting)
    void startSetting() {
        IntentUtils.startActivity(mContext, UserSettingActivity.class);
    }

    @OnClick(R.id.main_switch_login)
    void switchLogin() {
        IntentUtils.startActivity(mContext, FaceDetectExpActivity.class, null, Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_CLEAR_TOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
