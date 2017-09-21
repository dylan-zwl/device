package com.tapc.platform.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.ui.widget.BaseView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/4.
 */

public class RunInforTitleView extends BaseView {
    @BindView(R.id.run_infor_name)
    TextView mNameTv;
    @BindView(R.id.run_infor_icon)
    ImageView mIcon;

    public RunInforTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RunInfor);
        int icon = array.getResourceId(R.styleable.RunInfor_runinforIcon, -1);
        if (icon != -1) {
            mIcon.setImageResource(icon);
        }
        initTvShow(mNameTv, array, R.styleable.RunInfor_runinforName);
    }

    @Override
    protected int getContentView() {
        return R.layout.view_run_infor_title;
    }

    private void initTvShow(TextView textView, TypedArray array, int styleId) {
        String text = array.getString(styleId);
        if (text != null) {
            textView.setText(text);
        }
    }
}
