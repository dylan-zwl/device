package com.tapc.platform.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tapc.platform.R;

/**
 * Created by Administrator on 2017/9/12.
 */

public class Histogram extends View {
    private int mBgFrameColor;
    private float mShowHeight;
    private float mMaxHeight;

    public Histogram(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgramChart);
        mBgFrameColor = array.getColor(R.styleable.ProgramChart_bgFrameColor, 0);
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(mBgFrameColor);
        paint.setAntiAlias(true);
        float maxHeight = getHeight();
        float height = mShowHeight * maxHeight / mMaxHeight;
        canvas.drawRect(new RectF(0, maxHeight - height, getWidth(), maxHeight), paint);
    }

    public void setShowHeight(float mShowHeight) {
        this.mShowHeight = mShowHeight;
    }

    public void setMaxHeight(float maxHeight) {
        this.mMaxHeight = maxHeight;
    }
}
