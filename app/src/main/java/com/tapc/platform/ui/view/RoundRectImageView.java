package com.tapc.platform.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tapc.platform.R;

/**
 * Created by Administrator on 2017/8/30.
 */

public class RoundRectImageView extends ImageView {
    private static final int MODE_CIRCLE = 0;
    private static final int MODE_ROUNDRECT = 1;
    private Paint mPaint;
    private int mRoundMode;
    private int mRadius;

    public RoundRectImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundRect);
        mRoundMode = array.getInt(R.styleable.RoundRect_round_rect_mode, MODE_CIRCLE);
        mRadius = array.getInt(R.styleable.RoundRect_round_rect_radius, 12);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (null != drawable) {
            Bitmap bitmap = drawableToBitmap(drawable);
            bitmap = resizeImage(bitmap, getWidth(), getHeight());
            Bitmap roundBitmap = null;
            switch (mRoundMode) {
                case MODE_CIRCLE:
                    roundBitmap = getCircleBitmap(bitmap);
                    break;
                case MODE_ROUNDRECT:
                    roundBitmap = getRoundRectBitmap(bitmap, mRadius);
                    break;
            }
            if (roundBitmap != null) {
                final Rect rect = new Rect(0, 0, roundBitmap.getWidth(), roundBitmap.getHeight());
                mPaint.reset();
                canvas.drawBitmap(roundBitmap, rect, rect, mPaint);
            }
        } else {
            super.onDraw(canvas);
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    // 使用Bitmap加Matrix来缩放
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        float scaleWidth = ((float) w) / bitmap.getWidth();
        float scaleHeight = ((float) h) / bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * * 获取圆形图片方法
     *
     * @param bitmap
     * @return Bitmap
     * @author caizhiming
     */
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        int width = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Canvas canvas = new Canvas(output);
        canvas.drawCircle(width / 2, width / 2, width / 2, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, mPaint);
        return output;
    }


    private Bitmap getRoundRectBitmap(Bitmap bitmap, int radius) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectf = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        int width = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Canvas canvas = new Canvas(output);
        canvas.drawRoundRect(rectf, radius, radius, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, mPaint);
        return output;
    }
}
