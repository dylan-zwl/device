package com.tapc.platform.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.tapc.platform.R;
import com.tapc.platform.application.TapcApplication;
import com.tapc.platform.entity.WidgetShowStatus;
import com.tapc.platform.service.StartService;
import com.tapc.platform.ui.activity.run.RunInforActivity;
import com.tapc.platform.utils.IntentUtils;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/7.
 */

public class ShortcutKey extends BaseView implements View.OnTouchListener {
    @BindView(R.id.shortcut_area)
    LinearLayout mShortcutarea;
    @BindView(R.id.shortcut_ll)
    LinearLayout mShortcutKeyLL;
    @BindView(R.id.shortcut_key)
    Button mShortcutKeyBtn;
    @BindView(R.id.shortcut_program)
    CheckBox mProgramChx;
    @BindView(R.id.shortcut_runinfor_bar)
    CheckBox mRunInforBarChx;

    private StartService service;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowManagerParams;
    private boolean isMove = false;
    private float mTouchX;
    private float mTouchY;
    private float moveToBottom;

    public ShortcutKey(Context context, WindowManager windowManager, WindowManager.LayoutParams windowManagerParams) {
        super(context);
        service = TapcApplication.getInstance().getService();
        mWindowManager = windowManager;
        mWindowManagerParams = windowManagerParams;
        mShortcutKeyBtn.setOnTouchListener(this);
        moveToBottom = 1080 - getResources().getDimension(R.dimen.bottom_bar_h);
    }

    @Override
    protected int getContentView() {
        return R.layout.widget_shortcut_key;
    }

    @OnClick(R.id.shortcut_key)
    void shortcutKey() {
        if (isMove) {
            return;
        }
        if (mShortcutKeyLL.isShown()) {
            mShortcutKeyLL.setVisibility(GONE);
            mShortcutKeyLL.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_left_in));
        } else {
            mShortcutKeyLL.setVisibility(VISIBLE);
            mShortcutKeyLL.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_right_out));
        }
    }

    @OnCheckedChanged(R.id.shortcut_program)
    void program(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            service.setProgramStageDialogVisibility(WidgetShowStatus.VISIBLE);
        } else {
            service.setProgramStageDialogVisibility(WidgetShowStatus.GONE);
        }
    }

    @OnCheckedChanged(R.id.shortcut_runinfor_bar)
    void runInforBar(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            service.setRunInforBarVisibility(WidgetShowStatus.VISIBLE);
        } else {
            service.setRunInforBarVisibility(WidgetShowStatus.GONE);
        }
    }

    @OnCheckedChanged(R.id.shortcut_runinfor)
    void runInforActivity(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            service.setAppBarVisibility(WidgetShowStatus.GONE);
            mProgramChx.setChecked(false);
            mRunInforBarChx.setChecked(false);
        } else {
            service.setAppBarVisibility(WidgetShowStatus.VISIBLE);
            mProgramChx.setChecked(true);
            mRunInforBarChx.setChecked(true);
        }
        IntentUtils.startActivity(mContext, RunInforActivity.class, null, Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_CLEAR_TOP);
    }

    @OnClick(R.id.shortcut_clear_app)
    void clearApp() {

    }

    @OnCheckedChanged(R.id.shortcut_screen)
    void screen(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            service.setAppBarVisibility(WidgetShowStatus.VISIBLE);
            service.setBottomBarVisibility(WidgetShowStatus.VISIBLE);
            mProgramChx.setChecked(true);
            mRunInforBarChx.setChecked(true);
        } else {
            service.setAppBarVisibility(WidgetShowStatus.GONE);
            service.setBottomBarVisibility(WidgetShowStatus.REMOVE);
            mProgramChx.setChecked(false);
            mRunInforBarChx.setChecked(false);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.shortcut_key:
                if (mShortcutKeyLL.isShown() == false) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mTouchX = event.getRawX();
                            mTouchY = event.getRawY();
                            isMove = false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float moveX = mTouchX - event.getRawX();
                            float moveY = mTouchY - event.getRawY();
                            float needMove = 20;
                            if (Math.abs(moveX) > needMove || Math.abs(moveY) > needMove) {
                                int x = (int) (event.getRawX() - mShortcutarea.getWidth() / 2);
                                int y = (int) (event.getRawY() - mShortcutarea.getHeight() / 2);
                                if (y < (moveToBottom - mShortcutarea.getHeight())) {
                                    updateViewLayout(x, y);
                                    isMove = true;
                                }
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                    }
                }
                break;
        }
        return false;
    }

    // 刷新显示
    private void updateViewLayout(int x, int y) {
        mWindowManagerParams.x = x;
        mWindowManagerParams.y = y;
        mWindowManager.updateViewLayout(this, mWindowManagerParams);
    }
}
