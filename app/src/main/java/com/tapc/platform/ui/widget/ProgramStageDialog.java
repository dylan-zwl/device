package com.tapc.platform.ui.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.tapc.platform.R;
import com.tapc.platform.ui.view.BaseSystemView;
import com.tapc.platform.utils.WindowManagerUtils;

/**
 * Created by Administrator on 2017/9/13.
 */

public class ProgramStageDialog extends BaseSystemView {

    public ProgramStageDialog(Context context) {
        super(context);
    }

    @Override
    public WindowManager.LayoutParams getLayoutParams() {
        return WindowManagerUtils.getLayoutParams(36, 132, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity
                .TOP | Gravity.LEFT);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_program_stage;
    }
}
