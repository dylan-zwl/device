package com.tapc.platform.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.tapc.platform.R;
import com.tapc.platform.ui.widget.BaseView;

import butterknife.BindView;

public class ProgramImagView extends BaseView {
    @BindView(R.id.program_delete)
    Button deleteBtn;

    public ProgramImagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContentView() {
        return R.layout.view_program_imageview;
    }

    public void setDeleteOnClick(OnClickListener l) {
        deleteBtn.setOnClickListener(l);
    }

    public void setDeleteBtnVisibility(boolean visibility) {
        deleteBtn.setVisibility(GONE);
    }
}
