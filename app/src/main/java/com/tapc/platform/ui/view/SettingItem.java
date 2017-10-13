package com.tapc.platform.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.tapc.platform.R;
import com.tapc.platform.ui.widget.BaseView;
import com.tapc.platform.utils.TypedArrayUtils;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

/**
 * Created by Administrator on 2017/10/10.
 */

public class SettingItem extends BaseView {
    @BindView(R.id.setting_item_icon)
    CheckBox mIcon;
    @BindView(R.id.setting_item_name)
    CheckBox mName;

    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Commons);
        TypedArrayUtils.setTextView(mName, array, R.styleable.Commons_name);
        TypedArrayUtils.setCheckBoxButton(mName, array, R.styleable.Commons_name);
        array.recycle();
    }

    @Override
    protected int getContentView() {
        return R.layout.view_setting_item;
    }

    @OnCheckedChanged(R.id.setting_item)
    void settingItemOnClick(CompoundButton buttonView, boolean isChecked) {

    }

    public void setChecked(boolean isChecked) {
        if (isChecked) {
            mName.setTextColor(getResources().getColor(R.color.commonColor1));
        } else {
            mName.setTextColor(getResources().getColor(R.color.commonColor2));
        }
        mIcon.setChecked(isChecked);
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        mIcon.setOnCheckedChangeListener(listener);
    }
}
