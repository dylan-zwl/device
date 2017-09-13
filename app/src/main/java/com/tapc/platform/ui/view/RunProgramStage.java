package com.tapc.platform.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import com.tapc.platform.R;
import com.tapc.platform.ui.widget.BaseView;

/**
 * Created by Administrator on 2017/9/13.
 */

public class RunProgramStage extends BaseView {
    public RunProgramStage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContentView() {
        return R.layout.run_program_stage;
    }
}
