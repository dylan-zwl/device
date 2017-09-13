package com.tapc.platform.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tapc.platform.R;
import com.tapc.platform.application.TapcApplication;
import com.tapc.platform.ui.activity.stop.StopActivity;
import com.tapc.platform.ui.view.DeviceCtl;
import com.tapc.platform.ui.view.FastSetDeviceCtl;
import com.tapc.platform.utils.IntentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/28.
 */

public class BottomBar extends BaseView {
    @BindView(R.id.bottombar_left_ctl)
    DeviceCtl mLeftDeviceCtl;
    @BindView(R.id.bottombar_right_ctl)
    DeviceCtl mRightDeviceCtl;
    @BindView(R.id.bottombar_ctl_ll)
    LinearLayout mCtlLL;
    @BindView(R.id.bottombar_fast_set_ctl)
    FastSetDeviceCtl mFastSetDeviceCtl;

    @BindView(R.id.bottombar_resume)
    Button mResumeBtn;
    @BindView(R.id.bottombar_pause)
    Button mPauseBtn;

    @Override
    protected int getContentView() {
        return R.layout.widget_bottom_bar;
    }

    public BottomBar(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        super.initView();
        mLeftDeviceCtl.setOnClickListener(new DeviceCtl.Listener() {
            @Override
            public void onAddClick() {

            }

            @Override
            public void onSubClick() {

            }

            @Override
            public void setDeviceValue(double value) {

            }

            @Override
            public void onCtlTypeClick(int icon) {
                setFastSetDeviceCtlType(icon);
                setFastSetCtlVisibility(true);
            }
        });

        mRightDeviceCtl.setOnClickListener(new DeviceCtl.Listener() {
            @Override
            public void onAddClick() {

            }

            @Override
            public void onSubClick() {

            }

            @Override
            public void setDeviceValue(double value) {

            }

            @Override
            public void onCtlTypeClick(int icon) {
                setFastSetDeviceCtlType(icon);
                setFastSetCtlVisibility(true);
            }
        });
        List<String> list = new ArrayList<>();
        list.add("3");
        list.add("6");
        list.add("9");
        list.add("12");
        list.add("15");
        list.add("3");
        list.add("6");
        list.add("9");
        list.add("12");
        list.add("15");
        list.add("3");
        list.add("6");
        list.add("9");
        list.add("12");
        list.add("15");
        mFastSetDeviceCtl.updateShow(list);
        mFastSetDeviceCtl.setListener(new FastSetDeviceCtl.Listener() {
            @Override
            public void onValueClick(String value) {
                Log.d("set value", "value");
            }

            @Override
            public void close() {
                setFastSetCtlVisibility(false);
            }
        });
    }

    private void setFastSetDeviceCtlType(int icon) {
        mFastSetDeviceCtl.setBackgroundResource(icon);
    }

    private void setFastSetCtlVisibility(boolean visibility) {
        if (visibility) {
            mFastSetDeviceCtl.setVisibility(VISIBLE);
            mCtlLL.setVisibility(INVISIBLE);
            mFastSetDeviceCtl.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fast_ctl_push_in));
            mCtlLL.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fast_ctl_push_out));
        } else {
            mFastSetDeviceCtl.setVisibility(GONE);
            mCtlLL.setVisibility(VISIBLE);
            mFastSetDeviceCtl.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fast_ctl_push_out));
            mCtlLL.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fast_ctl_push_in));
        }
    }

    @OnClick(R.id.bottombar_pause)
    void pause() {
        mResumeBtn.setVisibility(VISIBLE);
        mPauseBtn.setVisibility(GONE);
    }

    @OnClick(R.id.bottombar_resume)
    void resume() {
        mResumeBtn.setVisibility(GONE);
        mPauseBtn.setVisibility(VISIBLE);
    }

    @OnClick(R.id.bottombar_stop)
    void stop() {
        IntentUtils.startActivity(mContext, StopActivity.class, null, Intent.FLAG_ACTIVITY_NEW_TASK);
        TapcApplication.getInstance().getService().setBottomBarVisibility(false);
        TapcApplication.getInstance().getService().setAppBarVisibility(false);
        TapcApplication.getInstance().getService().setRunInforBarVisibility(false);
        TapcApplication.getInstance().getService().setProgramStageDialogVisibility(false);
    }

    @OnClick(R.id.bottombar_back)
    void backOnClick() {
        TapcApplication.getInstance().getKeyEvent().backEvent();
    }

    @OnClick(R.id.bottombar_home)
    void homeOnClick() {
        IntentUtils.home(mContext);
        // TapcApp.getmInstance().keyboardEvent.homeEvent();
    }
}
