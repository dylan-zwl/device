package com.tapc.platform.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager;

import com.tapc.platform.R;
import com.tapc.platform.library.workouting.WorkOuting;
import com.tapc.platform.ui.view.BaseSystemView;
import com.tapc.platform.utils.WindowManagerUtils;

import java.io.IOException;

import butterknife.BindView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2017/9/29.
 */

public class CountdownDialog extends BaseSystemView {
    @BindView(R.id.countdown_iv)
    GifImageView mCountdown;

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
//        Glide.with(mContext).load(R.drawable.gif_countdown).asGif().skipMemoryCache(true).diskCacheStrategy
//                (DiskCacheStrategy.NONE).into(mCountdown);
        try {
            GifDrawable gifFromResource = new GifDrawable(getResources(), R.drawable.gif_countdown);
            mCountdown.setImageDrawable(gifFromResource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mFinishedListener != null) {
                    mFinishedListener.onFinished();
                }
                dismiss();
                WorkOuting.getInstance().resume();
            }
        }, 5000);
    }

    @Override
    public WindowManager.LayoutParams getLayoutParams() {
        return WindowManagerUtils.getLayoutParams(0, 0, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity
                .TOP);
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
