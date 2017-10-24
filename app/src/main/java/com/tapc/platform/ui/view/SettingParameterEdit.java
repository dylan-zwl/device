package com.tapc.platform.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.ui.widget.BaseView;
import com.tapc.platform.utils.TypedArrayUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/23.
 */

public class SettingParameterEdit extends BaseView {
    @BindView(R.id.setting_function_name)
    TextView mName;
    @BindView(R.id.setting_parameter_et)
    EditText mEditText;

    @Override
    protected int getContentView() {
        return R.layout.view_setting_function_edit;
    }

    public SettingParameterEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Commons);
        TypedArrayUtils.setTextView(mName, array, R.styleable.Commons_name);
        array.recycle();
    }

    @Override
    protected void initView() {
        super.initView();
    }

    public void addTextChangedListener(TextWatcher watcher) {
        mEditText.addTextChangedListener(watcher);
    }

    public EditText getEditText() {
        return mEditText;
    }
}
