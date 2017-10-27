package com.tapc.platform.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.ui.widget.BaseView;
import com.tapc.platform.utils.TypedArrayUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/5.
 */

public class SettingDevInfor extends BaseView {
    @BindView(R.id.dev_infor_ic)
    ImageView mIc;
    @BindView(R.id.dev_infor_name)
    TextView mName;
    @BindView(R.id.dev_infor_text)
    TextView mText;
    @BindView(R.id.dev_infor_btn)
    Button mBtn;

    @Override
    protected int getContentView() {
        return R.layout.view_setting_dev_infor;
    }

    public SettingDevInfor(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray commonArray = context.obtainStyledAttributes(attrs, R.styleable.Commons);

        TypedArrayUtils.setImageView(mIc, commonArray, R.styleable.Commons_icons);
        TypedArrayUtils.setTextView(mName, commonArray, R.styleable.Commons_name);
        TypedArrayUtils.setTextView(mBtn, commonArray, R.styleable.Commons_btnName);
        boolean isShowBtn = commonArray.getBoolean(R.styleable.Commons_isShowBtn, false);
        if (isShowBtn) {
            mBtn.setVisibility(VISIBLE);
        } else {
            mBtn.setVisibility(GONE);
        }

        float size = commonArray.getDimension(R.styleable.Commons_txsize, 28);
        mName.setTextSize(size);
        mText.setTextSize(size);
        mBtn.setTextSize(size);

        commonArray.recycle();
    }

    public void setInfor(String text) {
        mText.setText(text);
    }

    public void setOnClickListener(@Nullable OnClickListener l) {
        mBtn.setOnClickListener(l);
    }
}
