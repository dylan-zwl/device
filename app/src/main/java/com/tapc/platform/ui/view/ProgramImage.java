package com.tapc.platform.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tapc.platform.R;

import java.util.ArrayList;
import java.util.List;

public class ProgramImage extends View {
    private Paint mPain;
    private int mBgFrameColor;
    private int mBlockColor;
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
    private int mSpacingW;
    private int mSpacingH;

    private List<Float> mBlockList;
    private List<Float> mLineList;

    public ProgramImage(Context context) {
        super(context);
        init(context, null);
    }

    public ProgramImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgramImage);
        mBgFrameColor = array.getColor(R.styleable.ProgramImage_bgFrameColor, 0);
        mLineColor = array.getColor(R.styleable.ProgramImage_lineColor, 0);
        mBgFrameRadius = array.getDimension(R.styleable.ProgramImage_bgFrameRadius, 12);
        mBlockColor = array.getColor(R.styleable.ProgramImage_blockColor, 0);
        mPaddingLeft = array.getDimension(R.styleable.ProgramImage_paddingLeft, 0);
        mPaddingRight = array.getDimension(R.styleable.ProgramImage_paddingRight, 0);
        mPaddingTop = array.getDimension(R.styleable.ProgramImage_paddingTop, 0);
        mPaddingBottom = array.getDimension(R.styleable.ProgramImage_paddingBottom, 0);

        mBlockW = array.getDimension(R.styleable.ProgramImage_blockWith, 0);
        mBlockH = array.getDimension(R.styleable.ProgramImage_blockHeight, 0);

        mPain = new Paint();
        mPain.setAntiAlias(true);

        mBlockList = new ArrayList<>();
        mBlockList.add((float) 1);
        mBlockList.add((float) 2);
        mBlockList.add((float) 3);
        mBlockList.add((float) 4);
        mLineList = mBlockList;
        mRow = 20;
        mColumn = 16;
        mBlockW = 8;
        mBlockH = 5;

        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //背景框
        mPain.setStyle(Paint.Style.STROKE);
        mPain.setStrokeWidth(2);
        mPain.setColor(mBgFrameColor);
        mShowW = getWidth();
        mShowH = getHeight();
        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(rectF, mBgFrameRadius, mBgFrameRadius, mPain);

        //画方块
        mPain.setStyle(Paint.Style.FILL);
        mPain.setColor(mBlockColor);
        if (mBlockList == null || mLineList == null || mColumn == 0) {
            return;
        }
        mSpacingW = (int) ((mShowW - mPaddingLeft - mPaddingRight - mBlockW * mColumn) / (mColumn - 1));
        mSpacingH = (int) ((mShowH - mPaddingTop - mPaddingBottom - mBlockH * mRow) / (mRow - 1));

        for (int column = 0; column < mColumn; column++) {
            if (column > (mBlockList.size() - 1)) {
                break;
            }
            int showRow = mBlockList.get(column).intValue();
            for (int row = 0; row < showRow; row++) {
                int left = (int) (mBlockW * column + mSpacingW * column + mPaddingLeft);
                int top = (int) (mBlockH * (mRow - row - 1) + mSpacingH * (mRow - row) + mPaddingTop);
                canvas.drawRect(left, top, left + mBlockW, top + mBlockH, mPain);
            }
        }
        Path path = new Path();
        mPain.setStyle(Paint.Style.FILL);
        mPain.setColor(mLineColor);

        path.moveTo(mPaddingLeft + mBlockW / 2, mShowH - mPaddingBottom - mBlockH / 2);
        for (int column = 0; column < mColumn; column++) {
            if (column > (mLineList.size() - 1)) {
                break;
            }
            int showRow = mLineList.get(column).intValue() - 1;
            int left = (int) (mBlockW * column + mSpacingW * column + mPaddingLeft);
            int top = (int) (mBlockH * (mRow - showRow - 1) + mSpacingH * (mRow - showRow) + mPaddingTop);
            int x = (int) (left + mBlockW / 2);
            int y = (int) (top + mBlockH / 2);
            path.lineTo(x, y);
//            canvas.drawCircle(x, y, 3, mPain);
        }

        mPain.setStyle(Paint.Style.STROKE);
        mPain.setStrokeWidth(2);
        mPain.setColor(mLineColor);
        canvas.drawPath(path, mPain);
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
}
