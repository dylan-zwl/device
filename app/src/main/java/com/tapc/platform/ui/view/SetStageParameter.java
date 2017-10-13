package com.tapc.platform.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.ui.widget.BaseView;
import com.tapc.platform.utils.FormatUtils;

import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/12.
 */

public class SetStageParameter extends BaseView {
    @BindView(R.id.set_stage_parameter_name_tv)
    TextView mNameTv;
    @BindView(R.id.set_stage_parameter_unit_tv)
    TextView mUnitTv;
    @BindView(R.id.set_stage_parameter_value_tv)
    TextView mValueTv;
    @BindView(R.id.set_stage_parameter_sbar)
    SeekBar mSeekBar;

    private int mStepValue;
    private int mBits;
    private int mBitValue = 1;
    private int mMinValue;
    private int mMaxValue;
    private ValueListener mValueListener;

    public SetStageParameter(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Commons);
        initTvShow(mNameTv, array, R.styleable.Commons_name);
        initTvShow(mUnitTv, array, R.styleable.Commons_unit);
        initTvShow(mValueTv, array, R.styleable.Commons_value);
    }

    @Override
    protected int getContentView() {
        return R.layout.view_set_stage_parameter;
    }

    private void initTvShow(TextView textView, TypedArray array, int styleId) {
        String text = array.getString(styleId);
        if (text != null) {
            textView.setText(text);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float progressF = (progress + mMinValue) * mStepValue;
                float showValue = progressF / mBitValue;
                String value = "";
                if (mBits == 0) {
                    value = String.format("%.0f", Float.valueOf(showValue));
                } else {
                    value = String.valueOf(FormatUtils.formatFloat(mBits, showValue, RoundingMode.HALF_DOWN));
                }
                mValueTv.setText(value);
                if (mValueListener != null) {
                    mValueListener.onChangeValue(Float.valueOf(value));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void initShow(float minValue, float maxValue, float stepValue, int bits) {
        int bitValue = (int) Math.pow(10, bits);
        mMinValue = (int) (minValue * bitValue);
        mMaxValue = (int) (maxValue * bitValue);
        mStepValue = (int) (stepValue * bitValue);
        mSeekBar.setMax((mMaxValue - mMinValue) / mStepValue);
        mBitValue = bitValue;
        mBits = bits;
    }

    @OnClick(R.id.set_stage_parameter_add_btn)
    void addOnClick() {
        mSeekBar.setProgress(mSeekBar.getProgress() + 1);
    }

    @OnClick(R.id.set_stage_parameter_sub_btn)
    void subOnClick() {
        mSeekBar.setProgress(mSeekBar.getProgress() - 1);
    }

    public void setValue(float progress) {
        int setProgress = (int) (progress * mBitValue - mMinValue) / mStepValue;
        mSeekBar.setProgress(setProgress);
    }


    public void setValueListener(ValueListener valueListener) {
        this.mValueListener = valueListener;
    }

    public interface ValueListener {
        void onChangeValue(float value);
    }
}
