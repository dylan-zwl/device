package com.tapc.platform.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.ui.widget.BaseView;
import com.tapc.platform.utils.TypedArrayUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/23.
 */

public class SettingFunctionBtn extends BaseView {
    @BindView(R.id.setting_function_name)
    TextView mName;
    @BindView(R.id.setting_function_tbtn)
    Button mBtn;


    @Override
    protected int getContentView() {
        return R.layout.view_setting_function_btn;
    }

    public SettingFunctionBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Commons);
        TypedArrayUtils.setTextView(mName, array, R.styleable.Commons_name);
        array.recycle();
    }

    @Override
    protected void initView() {
        super.initView();
    }

    void setOnCheckedChange(@Nullable OnClickListener l) {
        mBtn.setOnClickListener(l);
    }

}
