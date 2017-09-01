package com.tapc.platform.ui.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.tapc.platform.R;
import com.tapc.platform.ui.widget.BaseView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/31.
 */

public class KeyboardView extends BaseView implements View.OnClickListener {
    @BindView(R.id.keyboard_0)
    Button mKeyboard0;
    @BindView(R.id.keyboard_1)
    Button mKeyboard1;
    @BindView(R.id.keyboard_2)
    Button mKeyboard2;
    @BindView(R.id.keyboard_3)
    Button mKeyboard3;
    @BindView(R.id.keyboard_4)
    Button mKeyboard4;
    @BindView(R.id.keyboard_5)
    Button mKeyboard5;
    @BindView(R.id.keyboard_6)
    Button mKeyboard6;
    @BindView(R.id.keyboard_7)
    Button mKeyboard7;
    @BindView(R.id.keyboard_8)
    Button mKeyboard8;
    @BindView(R.id.keyboard_9)
    Button mKeyboard9;
    @BindView(R.id.keyboard_dot)
    Button mKeyboardDot;
    @BindView(R.id.keyboard_x)
    Button mKeyboardX;

    private String mValue = "";
    private int mBits;


    @Override
    protected int getContentView() {
        return R.layout.view_keyboard;
    }

    public KeyboardView(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        super.initView();
        mKeyboard0.setOnClickListener(this);
        mKeyboard1.setOnClickListener(this);
        mKeyboard2.setOnClickListener(this);
        mKeyboard3.setOnClickListener(this);
        mKeyboard4.setOnClickListener(this);
        mKeyboard5.setOnClickListener(this);
        mKeyboard6.setOnClickListener(this);
        mKeyboard7.setOnClickListener(this);
        mKeyboard8.setOnClickListener(this);
        mKeyboard9.setOnClickListener(this);
        mKeyboardDot.setOnClickListener(this);
        mKeyboardX.setOnClickListener(this);
    }

    public void setValue(String value, int bits) {
        mValue = value;
        mBits = bits;
    }

    @Override
    public void onClick(View v) {
        String clickValue = "";
        switch (v.getId()) {
            case R.id.keyboard_0:
                clickValue = "0";
                break;
            case R.id.keyboard_1:
                clickValue = "1";
                break;
            case R.id.keyboard_2:
                clickValue = "2";
                break;
            case R.id.keyboard_3:
                clickValue = "3";
                break;
            case R.id.keyboard_4:
                clickValue = "4";
                break;
            case R.id.keyboard_5:
                clickValue = "5";
                break;
            case R.id.keyboard_6:
                clickValue = "6";
                break;
            case R.id.keyboard_7:
                clickValue = "7";
                break;
            case R.id.keyboard_8:
                clickValue = "8";
                break;
            case R.id.keyboard_9:
                clickValue = "9";
                break;
            case R.id.keyboard_dot:
                if (mValue.contains(".") == false) {
                    clickValue = ".";
                }
                break;
            case R.id.keyboard_x:
                if (!mValue.isEmpty()) {
                    mValue = mValue.substring(0, mValue.length() - 1);
                } else {
                    mValue = "";
                }
                break;
        }
        String tempValue = mValue + clickValue;
        if (tempValue.length() <= mBits) {
            mValue = tempValue;
            if (mListener != null) {
                mListener.onValueChange(mValue);
            }
        }
    }

    private KeyboardListener mListener;

    public void setListener(KeyboardListener listener) {
        this.mListener = listener;
    }

    public interface KeyboardListener {
        void onValueChange(String value);
    }
}
