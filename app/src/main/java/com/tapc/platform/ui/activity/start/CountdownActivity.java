package com.tapc.platform.ui.activity.start;

import android.content.Intent;
import android.os.Handler;

import com.tapc.platform.R;
import com.tapc.platform.entity.RunType;
import com.tapc.platform.ui.activity.BaseActivity;
import com.tapc.platform.ui.activity.run.RunCommonActivity;
import com.tapc.platform.ui.activity.run.RunVaActivity;

import java.io.IOException;

import butterknife.BindView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.tapc.platform.entity.RunType.NOMAL;

/**
 * Created by Administrator on 2017/9/7.
 */

public class CountdownActivity extends BaseActivity {
    @BindView(R.id.countdown_iv)
    GifImageView mCountdown;

    private Handler mHandler;
    private Intent mIntent;
    private Class<?> mStartCls;

    @Override
    protected int getContentView() {
        return R.layout.activity_countdown;
    }

    @Override
    protected void initView() {
        super.initView();
//        Glide.with(this).load(R.drawable.gif_countdown).skipMemoryCache(true).diskCacheStrategy
//                (DiskCacheStrategy.SOURCE).into(mCountdown);
        try {
            GifDrawable gifFromResource = new GifDrawable(getResources(), R.drawable.gif_countdown);
            mCountdown.setImageDrawable(gifFromResource);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIntent.setClass(mContext, mStartCls);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);
                finish();
            }
        }, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
