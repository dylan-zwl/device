package com.tapc.platform.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.ui.widget.BaseView;
import com.tapc.platform.utils.TypedArrayUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/10.
 */

public class TopTitle extends BaseView {
    @BindView(R.id.title_text)
    TextView mTitleTv;

    @BindView(R.id.title_back)
    ImageView mBack;

    public TopTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Commons);
        TypedArrayUtils.setTextView(mTitleTv, array, R.styleable.Commons_name);
        boolean isShowBack = array.getBoolean(R.styleable.Commons_isShowBtn, false);
        setShowBack(isShowBack);
        array.recycle();
    }

    public void setShowBack(boolean visibility) {
        if (visibility) {
            mBack.setVisibility(VISIBLE);
        } else {
            mBack.setVisibility(GONE);
        }
    }

    public void setTitleOnClickListener(@Nullable OnClickListener l) {
        mTitleTv.setOnClickListener(l);
    }

    public void setTitleClickable(boolean clickable) {
        mTitleTv.setClickable(clickable);
    }

    @Override
    protected int getContentView() {
        return R.layout.view_title;
    }

    public void setBackListener(@Nullable OnClickListener l) {
        mBack.setOnClickListener(l);
    }
}
