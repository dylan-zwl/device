package com.tapc.platform.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.application.TapcApplication;
import com.tapc.platform.library.workouting.WorkOuting;
import com.tapc.platform.ui.view.BaseSystemView;
import com.tapc.platform.utils.WindowManagerUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/29.
 */

public class CountdownDialog extends BaseSystemView {
    //    @BindView(R.id.countdown_iv)
//    GifImageView mCountdown;
    @BindView(R.id.countdown_iv)
    TextView mCountdown;

    private Handler mHandler;
    private FinishedListener mFinishedListener;
    private int mNumbers;

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
//        try {
//            GifDrawable gifFromResource = new GifDrawable(getResources(), R.drawable.gif_countdown);
//            mCountdown.setImageDrawable(gifFromResource);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void show() {
        super.show();
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
        }, 4000);
        startTimer();
        ShortcutKey shortcutKey = TapcApplication.getInstance().getService().getShortcutKey();
        if (shortcutKey != null) {
            shortcutKey.setFullScreen(true);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mCountdown.setText("");
        ShortcutKey shortcutKey = TapcApplication.getInstance().getService().getShortcutKey();
        if (shortcutKey != null) {
            shortcutKey.setFullScreen(false);
        }
    }

    private void startTimer() {
        mNumbers = 3;
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mNumbers <= 0) {
                            mCountdown.setText("GO");
                            timer.cancel();
                        } else {
                            mCountdown.setText("" + mNumbers);
                        }
                        mNumbers--;
                    }
                });
            }
        }, 0, 1000);
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
