package com.tapc.platform.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.tapc.platform.R;
import com.tapc.platform.application.TapcApplication;
import com.tapc.platform.model.ClickModel;
import com.tapc.platform.ui.activity.settings.system.SystemSettingActivity;
import com.tapc.platform.ui.view.BaseSystemView;
import com.tapc.platform.ui.view.TopTitle;
import com.tapc.platform.utils.IntentUtils;
import com.tapc.platform.utils.WindowManagerUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/26.
 */

public class SettingTopBar extends BaseSystemView {
    @BindView(R.id.setting_top_bar)
    TopTitle mTopBar;

    private ClickModel mClickModel;

    public SettingTopBar(Context context) {
        super(context);
    }

    @Override
    protected int getContentView() {
        return R.layout.widget_setting_top_bar;
    }

    @Override
    protected void initView() {
        super.initView();
        mClickModel = new ClickModel(1000, 5);
        mClickModel.setListener(new ClickModel.Listener() {
            @Override
            public void onClickCompleted() {
                IntentUtils.startActivity(mContext, SystemSettingActivity.class, null, Intent
                        .FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });

        mTopBar.setBackListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TapcApplication.getInstance().getKeyEvent().back();
            }
        });

        mTopBar.setTitleOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickModel.onClick();
            }
        });
    }

    public void setTitleClick(boolean clickable) {
        mTopBar.setTitleClickable(clickable);
    }

    @Override
    public WindowManager.LayoutParams getLayoutParams() {
        return WindowManagerUtils.getLayoutParams(0, 0, LayoutParams.MATCH_PARENT, (int) getResources().getDimension
                (R.dimen.setting_top_bar_h), Gravity.TOP | Gravity.CENTER_VERTICAL);
    }
}
