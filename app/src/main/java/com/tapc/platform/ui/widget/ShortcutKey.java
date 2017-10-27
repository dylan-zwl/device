package com.tapc.platform.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.tapc.platform.R;
import com.tapc.platform.application.TapcApplication;
import com.tapc.platform.service.StartService;
import com.tapc.platform.ui.activity.run.RunInforActivity;
import com.tapc.platform.ui.view.BaseSystemView;
import com.tapc.platform.utils.IntentUtils;
import com.tapc.platform.utils.WindowManagerUtils;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/7.
 */

public class ShortcutKey extends BaseSystemView implements View.OnTouchListener {
    @BindView(R.id.shortcut_area)
    LinearLayout mShortcutarea;
    @BindView(R.id.shortcut_ll)
    LinearLayout mShortcutKeyLL;
    @BindView(R.id.shortcut_key)
    ImageButton mShortcutKeyBtn;
    @BindView(R.id.shortcut_program)
    CheckBox mProgramChx;
    @BindView(R.id.shortcut_runinfor_bar)
    CheckBox mRunInforBarChx;

    private BottomBar mBottomBar;
    private AppBar mAppBar;
    private RunInforBar mRunInforBar;
    private ProgramStageDialog mProgramStageDialog;

    private StartService service;
    private boolean isMove = false;
    private float mTouchX;
    private float mTouchY;
    private float moveToBottom;

    private WindowManager.LayoutParams mWindowManagerParams;

    public ShortcutKey(Context context) {
        super(context);
        service = TapcApplication.getInstance().getService();
        mShortcutKeyBtn.setOnTouchListener(this);
        moveToBottom = 1080 - getResources().getDimension(R.dimen.bottom_bar_h);
    }

    @Override
    protected void initView() {
        super.initView();
        mBottomBar = new BottomBar(mContext);
        mAppBar = new AppBar(mContext);
        mRunInforBar = new RunInforBar(mContext);
        mProgramStageDialog = new ProgramStageDialog(mContext);
    }

    @Override
    protected int getContentView() {
        return R.layout.widget_shortcut_key;
    }

    private void initShow() {
        mShortcutKeyLL.setVisibility(GONE);
        mProgramChx.setChecked(mProgramStageDialog.isShown());
        mRunInforBarChx.setChecked(mRunInforBar.isShown());
    }


    @OnClick(R.id.shortcut_key)
    void shortcutKey() {
        if (isMove) {
            return;
        }
        if (mShortcutKeyLL.isShown()) {
            mShortcutKeyLL.setVisibility(GONE);
//            mShortcutKeyLL.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_left_out));
        } else {
            mShortcutKeyLL.setVisibility(VISIBLE);
            mShortcutKeyLL.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
        }
    }

    @OnCheckedChanged(R.id.shortcut_program)
    void program(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mProgramStageDialog.show();
        } else {
            mProgramStageDialog.hide();
        }
    }

    @OnCheckedChanged(R.id.shortcut_runinfor_bar)
    void runInforBar(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mRunInforBar.show();
        } else {
            mRunInforBar.hide();
        }
    }

    @OnCheckedChanged(R.id.shortcut_runinfor)
    void runInforActivity(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mAppBar.hide();
            mRunInforBar.hide();
            mProgramStageDialog.hide();

            mProgramChx.setVisibility(GONE);
            mRunInforBarChx.setVisibility(GONE);
        } else {
            mAppBar.show();
            if (mRunInforBarChx.isChecked()) {
                mRunInforBar.show();
            }
            if (mProgramChx.isChecked()) {
                mProgramStageDialog.show();
            }
            mProgramChx.setVisibility(VISIBLE);
            mRunInforBarChx.setVisibility(VISIBLE);
        }
        IntentUtils.startActivity(mContext, RunInforActivity.class, null, Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_CLEAR_TOP);
    }

    @OnClick(R.id.shortcut_clear_app)
    void clearApp() {

    }

    @OnCheckedChanged(R.id.shortcut_screen)
    void funScreen(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mAppBar.show();
            if (mRunInforBarChx.isChecked()) {
                mRunInforBar.show();
            }
            if (mProgramChx.isChecked()) {
                mProgramStageDialog.show();
            }
            mBottomBar.show();
        } else {
            mAppBar.hide();
            mRunInforBar.hide();
            mProgramStageDialog.hide();
            mBottomBar.dismiss();
//            mProgramChx.setChecked(false);
//            mRunInforBarChx.setChecked(false);
        }
    }

    @Override
    public void show() {
        super.show();
        mBottomBar.show();
        mAppBar.show();
        mRunInforBar.show();
        mProgramStageDialog.show();
        initShow();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mAppBar.dismiss();
        mRunInforBar.dismiss();
        mProgramStageDialog.dismiss();
        mBottomBar.dismiss();
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

    @Override
    public WindowManager.LayoutParams getLayoutParams() {
        mWindowManagerParams = WindowManagerUtils.getLayoutParams(36, 726, LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT, Gravity.TOP | Gravity.LEFT);
        return mWindowManagerParams;
    }
}
