package com.tapc.platform.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.ui.widget.BaseView;
import com.tapc.platform.utils.TypedArrayUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/4.
 */

public class RunInforView extends BaseView {
    @BindView(R.id.run_infor_name)
    TextView mNameTv;
    @BindView(R.id.run_infor_unit)
    TextView mUnitTv;
    @BindView(R.id.run_infor_value)
    TextView mValueTv;

    public RunInforView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RunInfor);
        TypedArrayUtils.setTextView(mNameTv, array, R.styleable.RunInfor_runinforName);
        TypedArrayUtils.setTextView(mUnitTv, array, R.styleable.RunInfor_runinforUint);
        TypedArrayUtils.setTextView(mValueTv, array, R.styleable.RunInfor_runinforValue);
        array.recycle();
    }

    public void setValue(String value) {
        if (value != null) {
            mValueTv.setText(value);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.view_run_infor;
    }
}
