package com.tapc.platform.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/3/22.
 */

public class QrcodeUtils {
    /**
     * 生成二维码图片
     */
    public static Bitmap createImage(String qrcodeStr, int width, int height, int minPandingSize) {
        if (width == 0 || height == 0 || TextUtils.isEmpty(qrcodeStr)) {
            return null;
        }
        Bitmap bitmap;
        try {
            int startX = 0;
            int startY = 0;
            boolean isFirstBlackPoint = false;

            QRCodeWriter writer = new QRCodeWriter();
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

            BitMatrix martix = writer.encode(qrcodeStr, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (martix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                        if (isFirstBlackPoint == false) {
                            isFirstBlackPoint = true;
                            startX = x;
                            startY = y;
                        }
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            // 剪切中间的二维码区域，减少padding区域
            if (startX <= minPandingSize)
                return bitmap;
            int x1 = startX - minPandingSize;
            int y1 = startY - minPandingSize;
            if (x1 < 0 || y1 < 0)
                return bitmap;
            int w1 = width - x1 * 2;
            int h1 = height - y1 * 2;
            Bitmap bitmapQR = Bitmap.createBitmap(bitmap, x1, y1, w1, h1);
            return bitmapQR;
        } catch (WriterException e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * 添加logo
     */
    public static Bitmap addQrLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }
        // 获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }
        // logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }

    /**
     * 显示二维码
     */
    public static void show(final String qrcodeStr, final ImageView imageView, final int minPandingSize,
                            ObservableTransformer composer) {
        if (TextUtils.isEmpty(qrcodeStr) || imageView == null ||
                imageView.getWidth() == 0 || imageView.getHeight() == 0) {
            return;
        }
        RxjavaUtils.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                e.onNext(QrcodeUtils.createImage(qrcodeStr, imageView.getWidth(), imageView.getHeight(),
                        minPandingSize));
                e.onComplete();
            }
        }, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                Bitmap bitmap = (Bitmap) o;
                if (bitmap != null && imageView != null) {
                    imageView.setBackground(new BitmapDrawable(bitmap));
                }
            }
        }, composer);
    }

}
