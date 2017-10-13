package com.tapc.platform.ui.activity.start;

import android.content.Intent;
import android.os.Handler;
import android.widget.ImageView;

import com.tapc.platform.R;
import com.tapc.platform.entity.RunType;
import com.tapc.platform.ui.activity.BaseActivity;
import com.tapc.platform.ui.activity.run.RunCommonActivity;
import com.tapc.platform.ui.activity.run.RunVaActivity;

import butterknife.BindView;

import static com.tapc.platform.entity.RunType.NOMAL;

/**
 * Created by Administrator on 2017/9/7.
 */

public class CountdownActivity extends BaseActivity {
    @BindView(R.id.countdown_iv)
    ImageView mCountdown;

    private Handler mHandler;
    private Intent mIntent;

    @Override
    protected int getContentView() {
        return R.layout.activity_countdown;
    }

    @Override
    protected void initView() {
        super.initView();
//        Glide.with(this).load(R.drawable.gif_countdown).skipMemoryCache(true).diskCacheStrategy
//                (DiskCacheStrategy.SOURCE).into(mCountdown);
//        try {
//            GifDrawable gifFromResource = new GifDrawable(mContext.getResources(), R.drawable.gif_countdown);
//            final MediaController mc = new MediaController(this);
//            mc.setMediaPlayer(gifFromResource);
//            mc.setAnchorView(mCountdown);
//            mc.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        mIntent = getIntent();
        RunType runType = (RunType) getIntent().getExtras().get("run_type");
        if (runType == null) {
            runType = NOMAL;
        }
        Class<?> cls = RunCommonActivity.class;
        switch (runType) {
            case NOMAL:
                cls = RunCommonActivity.class;
                break;
            case VA:
                cls = RunVaActivity.class;
                break;
        }
        final Class<?> finalCls = cls;
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIntent.setClass(mContext, finalCls);
                startActivity(mIntent);
//                IntentUtils.startActivity(mContext, finalCls, null, Intent.FLAG_ACTIVITY_NEW_TASK |
//                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
