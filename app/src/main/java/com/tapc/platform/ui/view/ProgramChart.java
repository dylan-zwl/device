package com.tapc.platform.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.tapc.platform.R;

import java.util.ArrayList;
import java.util.List;

public class ProgramChart extends View {
    private Paint mPain;
    private int mBgFrameColor;
    private int mBlockColor;
    private int mRunBlockColor;
    private int mLineColor;
    private float mBgFrameRadius;
    private float mPaddingLeft;
    private float mPaddingRight;
    private float mPaddingTop;
    private float mPaddingBottom;

    private int mShowW;
    private int mShowH;
    private float mBlockW;
    private float mBlockH;
    private int mColumn;
    private int mRow;
    private float mSpacingW;
    private float mSpacingH;

    private List<Float> mBlockList;
    private List<Float> mLineList;
    private int mPosition;

    public ProgramChart(Context context) {
        super(context);
        init(context, null);
    }

    public ProgramChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgramChart);
        mBgFrameColor = array.getColor(R.styleable.ProgramChart_bgFrameColor, 0);
        mLineColor = array.getColor(R.styleable.ProgramChart_lineColor, 0);
        mBgFrameRadius = array.getDimension(R.styleable.ProgramChart_bgFrameRadius, 12);
        mBlockColor = array.getColor(R.styleable.ProgramChart_blockColor, 0);
        mRunBlockColor = array.getColor(R.styleable.ProgramChart_runBlockColor, 0);
        mPaddingLeft = array.getDimension(R.styleable.ProgramChart_paddingLeft, 0);
        mPaddingRight = array.getDimension(R.styleable.ProgramChart_paddingRight, 0);
        mPaddingTop = array.getDimension(R.styleable.ProgramChart_paddingTop, 0);
        mPaddingBottom = array.getDimension(R.styleable.ProgramChart_paddingBottom, 0);

        mBlockW = array.getDimension(R.styleable.ProgramChart_blockWith, 0);
        mBlockH = array.getDimension(R.styleable.ProgramChart_blockHeight, 0);
        mPosition = -1;

        mPain = new Paint();
        mPain.setAntiAlias(true);

        mBlockList = new ArrayList<>();
        for (float i = 1; i <= 20; i++) {
            mBlockList.add(i);
        }
        mLineList = mBlockList;
        mRow = 20;
        mColumn = 20;
        mBlockW = 8;
        mBlockH = 5;
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mColumn == 0) {
            return;
        }

        mShowW = getWidth();
        mShowH = getHeight();
        /*背景框*/
//        mPain.setStyle(Paint.Style.STROKE);
//        mPain.setStrokeWidth(2);
//        mPain.setColor(mBgFrameColor);
//        RectF rectF = new RectF(0, 0, mShowW, mShowH);
//        canvas.drawRoundRect(rectF, mBgFrameRadius, mBgFrameRadius, mPain);

        mSpacingW = (mShowW - mPaddingLeft - mPaddingRight - mBlockW * mColumn) / (mColumn - 1);
        mSpacingH = 0;
//        mSpacingH = (int) ((mShowH - mPaddingTop - mPaddingBottom - mBlockH * mRow) / (mRow - 1));

        /* 矩形柱状*/
        if (mBlockList != null) {
            mPain.setStrokeWidth(2);
            mPain.setStyle(Paint.Style.FILL);
            mPain.setColor(mBlockColor);

                    /*格子*/
//        for (int column = 0; column < mColumn; column++) {
//            if (column > (mBlockList.size() - 1)) {
//                break;
//            }
//            int showRow = mBlockList.get(column).intValue();
//            for (int row = 0; row < showRow; row++) {
//                int left = (int) (mBlockW * column + mSpacingW * column + mPaddingLeft);
//                int top = (int) (mBlockH * (mRow - row - 1) + mSpacingH * (mRow - row) + mPaddingTop);
//                canvas.drawRect(left, top, left + mBlockW, top + mBlockH, mPain);
//            }
//        }

            for (int column = 0; column < mColumn; column++) {
                if (column > (mBlockList.size() - 1)) {
                    break;
                }
                int showRow = mBlockList.get(column).intValue();
                float rowHeight = mShowH - mPaddingTop - mPaddingBottom;
                float height = showRow * rowHeight / mRow;
                float weight = mBlockW;
                float left = (int) mBlockW * column + mSpacingW * column + mPaddingLeft;
                float top = (int) mPaddingTop + (rowHeight - height);
                if (column == (mPosition - 1)) {
                    Paint runColumnPaint = new Paint();
                    runColumnPaint.setAntiAlias(true);
                    runColumnPaint.setColor(mRunBlockColor);
                    runColumnPaint.setStyle(Paint.Style.FILL);
                    canvas.drawRect(left, top, left + weight, top + height, runColumnPaint);
                } else {
                    canvas.drawRect(left, top, left + weight, top + height, mPain);
                }
            }
        }

        /*画线*/
        if (mLineList != null) {
            Path path = new Path();
            mPain.setStyle(Paint.Style.FILL);
            mPain.setColor(mLineColor);
            mPain.setStrokeWidth(2);
            for (int column = 0; column < mColumn; column++) {
                if (column > (mLineList.size() - 1)) {
                    break;
                }
                int showRow = mLineList.get(column).intValue() - 1;
                float left = mBlockW * column + mSpacingW * column + mPaddingLeft;
                float top = (mRow - showRow) * mShowH / mRow;
                float x = left + mBlockW / 2;
                float y = top - (mShowH / mRow) / 2;
                if (column == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
//            描点
//            canvas.drawCircle(x, y, 3, mPain);
            }
            mPain.setStyle(Paint.Style.STROKE);
            mPain.setStrokeWidth(2);
            mPain.setColor(mLineColor);
            canvas.drawPath(path, mPain);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void init(int row, int column) {
        mRow = row;
        mColumn = column;
    }

    public void setBlockList(List<Float> list) {
        this.mBlockList = list;
    }

    public void setLineList(List<Float> list) {
        this.mLineList = list;
    }

    public void setRunBlock(int position) {
        mPosition = position;
    }
}
