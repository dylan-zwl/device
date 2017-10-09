package com.tapc.platform.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tapc.platform.R;
import com.tapc.platform.library.workouting.WorkOuting;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/29.
 */

public class CountdownDialog extends BaseView {
    @BindView(R.id.countdown_iv)
    ImageView mCountdown;

    private Handler mHandler;
    private FinishedListener mFinishedListener;

    public CountdownDialog(Context context) {
        super(context);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_countdown;
    }

    @Override
    protected void initView() {
        super.initView();
        Glide.with(mContext).load(R.drawable.gif_countdown).asGif().skipMemoryCache(true).diskCacheStrategy
                (DiskCacheStrategy.NONE).into(mCountdown);
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mFinishedListener != null) {
                    mFinishedListener.onFinished();
                }
                WorkOuting.getInstance().resume();
            }
        }, 5000);
    }

    public void setFinishedListener(FinishedListener finishedListener) {
        this.mFinishedListener = finishedListener;
    }

    public interface FinishedListener {
        void onFinished();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
