package com.tapc.platform.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.ui.widget.BaseView;

import butterknife.BindView;

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

    public SetStageParameter(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SetStageParameter);
        initTvShow(mNameTv, array, R.styleable.SetStageParameter_name);
        initTvShow(mUnitTv, array, R.styleable.SetStageParameter_unit);
        initTvShow(mValueTv, array, R.styleable.SetStageParameter_value);
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
}
