package com.tapc.platform.ui.widget;

import android.content.Context;

import com.tapc.platform.R;

/**
 * Created by Administrator on 2017/9/13.
 */

public class ProgramStageDialog extends BaseView {

    public ProgramStageDialog(Context context) {
        super(context);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_program_stage;
    }
}
