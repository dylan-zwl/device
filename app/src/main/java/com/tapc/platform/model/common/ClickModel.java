package com.tapc.platform.model.common;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/10/26.
 */

public class ClickModel {
    private Timer mTimer;
    private int mMaxClickNumbers;
    private int mCount;
    private int mTimerTimeOut;
    private Listener mListener;

    public ClickModel(int timerTimeOut, int max) {
        mCount = 0;
        mMaxClickNumbers = max;
        mTimerTimeOut = timerTimeOut;
    }

    public void onClick() {
        cancelTimer();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mCount = 0;
            }
        }, mTimerTimeOut);
        mCount++;
        if (mCount > mMaxClickNumbers) {
            reset();
            if (mListener != null) {
                mListener.onClickCompleted();
            }
        }
    }

    public void reset() {
        cancelTimer();
        mCount = 0;
    }

    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public interface Listener {
        void onClickCompleted();
    }
}
