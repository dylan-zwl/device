package com.tapc.platform.ui.activity.start;

import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.tapc.platform.R;
import com.tapc.platform.entity.WidgetShowStatus;
import com.tapc.platform.ui.activity.BaseActivity;
import com.tapc.platform.ui.activity.run.RunCommonActivity;
import com.tapc.platform.utils.IntentUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/7.
 */

public class CountdownActivity extends BaseActivity {
    @BindView(R.id.countdown_iv)
    ImageView mCountdown;

    private Handler mHandler;

    @Override
    protected int getContentView() {
        return R.layout.activity_countdown;
    }

    @Override
    protected void initView() {
        super.initView();
        Glide.with(this).load(R.drawable.gif_countdown).skipMemoryCache(true).fitCenter().centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(new GlideDrawableImageViewTarget(mCountdown, 1));
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                IntentUtils.startActivity(mContext, RunCommonActivity.class);
                finish();
            }
        }, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mTapcApp.getService().setBottomBarVisibility(WidgetShowStatus.VISIBLE);
    }
}
