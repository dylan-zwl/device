package com.tapc.platform.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tapc.platform.R;

/**
 * Created by Administrator on 2017/9/5.
 */

public class RoundProgressBar extends View {
    private Paint mPaint;
    private int mBgColor;
    private int mProgressColor;
    private float mRoundWidth;
    private int mMaxProgress;
    private int mProgress;

    public RoundProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        mBgColor = array.getColor(R.styleable.RoundProgressBar_bgColor, 0);
        mProgressColor = array.getColor(R.styleable.RoundProgressBar_progressColor, 1);
        mRoundWidth = array.getDimension(R.styleable.RoundProgressBar_roundWidth, 20);
        mMaxProgress = array.getInt(R.styleable.RoundProgressBar_maxProgress, 100);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mRoundWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centre = getWidth() / 2;
        int radius = (int) (centre - mRoundWidth / 2);

        mPaint.setColor(mBgColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawCircle(centre, centre, radius, mPaint);

        mPaint.setColor(mProgressColor);
        RectF rectF = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);
        canvas.drawArc(rectF, -90, 360 * ((float) mProgress) / mMaxProgress, false, mPaint);
    }

    public synchronized int getMax() {
        return mMaxProgress;
    }

    public synchronized void setMax(int max) {
        if (max > 0) {
            this.mMaxProgress = max;
        }
    }

    public synchronized void setProgress(int progress) {
        if (progress > 0) {
            if (progress > mMaxProgress) {
                progress = mMaxProgress;
            }
            if (progress <= mMaxProgress) {
                mProgress = progress;
                postInvalidate();
            }
        }
    }
}
