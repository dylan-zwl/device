package com.tapc.platform.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ProgramImage extends View {
    private Paint mBgPain;
    private Paint mBgBlockPain;
    private Paint mRunBlockPain;
    private Paint mOverBlockPain;
    private int mShowW;
    private int mShowH;
    private int mBlockW;
    private int mBlockH;
    private int mPaddingLeft;
    private int mPaddingTop;
    private int mColumn;
    private int mRow;
    private int mSpacingW;
    private int mSpacingH;
    private List<Integer> mProgramList;

    private List<Integer> mRunList;

    public ProgramImage(Context context) {
        super(context);
        init();
    }

    public ProgramImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mBgPain = new Paint();
        mBgPain.setStyle(Paint.Style.STROKE);
        mBgPain.setStrokeWidth(2);
        mBgPain.setColor(0x33ffffff);

        mBgBlockPain = new Paint();
        mBgBlockPain.setColor(Color.WHITE);

        mRunBlockPain = new Paint();
        mRunBlockPain.setColor(0xff78d0f0);

        mOverBlockPain = new Paint();
        mOverBlockPain.setColor(0xffd6ef00);

        mRunList = new ArrayList<>();
        mRunList.add(1);
        mRunList.add(2);
        mRunList.add(3);
        mRunList.add(4);
        mRow = 16;
        mColumn = 16;
        mBlockW = 8;
        mBlockH = 5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mRunList == null) {
            return;
        }
        RectF rectF = new RectF(0, 0, mShowW, mShowH);
        canvas.drawRoundRect(rectF, 12, 12, mBgPain);

        for (int column = 0; column < mColumn; column++) {
            if (column > (mRunList.size() - 1)) {
                break;
            }
            int showRow = mRunList.get(column);
            for (int row = 0; row < showRow; row++) {
                int left = mBlockW * column + mSpacingW * column + mPaddingLeft;
                int top = mBlockH * (mRow - row - 1) + mSpacingH * (mRow - row) + mPaddingTop;
                canvas.drawRect(left, top, left + mBlockW, top + mBlockH, mBgBlockPain);
            }
        }
    }

    public void init(int row, int column) {
        mRow = row;
        mColumn = column;
    }

    public void setSize(int weight, int height) {
        mShowW = weight;
        mShowH = height;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mShowW = w;
        mShowH = h;
        mPaddingLeft = (mShowW - mColumn * mBlockW - (mColumn - 1) * mSpacingW) / 2;
        mPaddingTop = (mShowH - mRow * mBlockH - (mRow - 1) * mSpacingH) / 2;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setRunList(List<Integer> runList) {
        this.mRunList = runList;
    }

}
