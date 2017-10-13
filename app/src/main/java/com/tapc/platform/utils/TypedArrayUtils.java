package com.tapc.platform.utils;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/10/10.
 */

public class TypedArrayUtils {
    public static void setTextView(TextView view, TypedArray array, int styleId) {
        String text = array.getString(styleId);
        if (text != null) {
            view.setText(text);
        }
    }

    public static void setImageView(ImageView view, TypedArray array, int styleId) {
        Drawable drawable = array.getDrawable(styleId);
        view.setBackground(drawable);
    }

    public static void setButtonView(Button view, TypedArray array, int styleId) {
        Drawable drawable = array.getDrawable(styleId);
        view.setBackground(drawable);
    }

    public static void setImageButton(ImageButton view, TypedArray array, int styleId) {
        Drawable drawable = array.getDrawable(styleId);
        view.setBackground(drawable);
    }

    public static void setCheckBoxButton(CheckBox view, TypedArray array, int styleId) {
        Drawable drawable = array.getDrawable(styleId);
        view.setButtonDrawable(drawable);
    }
}
