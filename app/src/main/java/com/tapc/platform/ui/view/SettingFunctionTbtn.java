package com.tapc.platform.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tapc.platform.R;
import com.tapc.platform.ui.widget.BaseView;
import com.tapc.platform.utils.TypedArrayUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/23.
 */

public class SettingFunctionTbtn extends BaseView {
    @BindView(R.id.setting_function_name)
    TextView mName;
    @BindView(R.id.setting_function_tbtn)
    ToggleButton mTBtn;

    @Override
    protected int getContentView() {
        return R.layout.view_setting_function;
    }

    public SettingFunctionTbtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Commons);
        TypedArrayUtils.setTextView(mName, array, R.styleable.Commons_name);
        TypedArrayUtils.setTextView(mTBtn, array, R.styleable.Commons_btnName);
        array.recycle();
    }

    @Override
    protected void initView() {
        super.initView();
    }

    public void setOnCheckedChange(CompoundButton.OnCheckedChangeListener listener) {
        mTBtn.setOnCheckedChangeListener(listener);
    }
}
