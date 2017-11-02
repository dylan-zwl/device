package com.tapc.platform.ui.activity.start;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.entity.RunType;
import com.tapc.platform.ui.activity.BaseActivity;
import com.tapc.platform.ui.activity.run.RunCommonActivity;
import com.tapc.platform.ui.activity.run.RunVaActivity;
import com.tapc.platform.ui.widget.CountdownDialog;

import butterknife.BindView;

import static com.tapc.platform.entity.RunType.NOMAL;

/**
 * Created by Administrator on 2017/9/7.
 */

public class CountdownActivity extends BaseActivity {
    @BindView(R.id.countdown_iv)
    TextView mCountdown;
    @BindView(R.id.countdown_ll)
    LinearLayout mCountdownLL;

    private Intent mIntent;
    private Class<?> mStartCls;

    @Override
    protected int getContentView() {
        return R.layout.activity_countdown;
    }

    @Override
    protected void initView() {
        super.initView();
        mCountdownLL.setVisibility(View.GONE);
        mTapcApp.getService().getCountdownDialog().setFinishedListener(new CountdownDialog.FinishedListener() {
            @Override
            public void onFinished() {
                mIntent.setClass(mContext, mStartCls);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);
                finish();
            }
        });
        mTapcApp.getService().getCountdownDialog().show();

        mIntent = getIntent();
        RunType runType = (RunType) getIntent().getExtras().get("run_type");
        if (runType == null) {
            runType = NOMAL;
        }
        mStartCls = RunCommonActivity.class;
        switch (runType) {
            case NOMAL:
                mStartCls = RunCommonActivity.class;
                break;
            case VA:
                mStartCls = RunVaActivity.class;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
