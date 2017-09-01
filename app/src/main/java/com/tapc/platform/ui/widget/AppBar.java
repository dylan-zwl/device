package com.tapc.platform.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tapc.platform.R;
import com.tapc.platform.ui.adpater.AppAdpater;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.ui.entity.AppInfoEntity;
import com.tapc.platform.utils.AppUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tapc.platform.ui.widget.AppBar.AppShowStatus.HIDE;
import static com.tapc.platform.ui.widget.AppBar.AppShowStatus.SHOW;


/**
 * Created by Administrator on 2017/8/28.
 */

public class AppBar extends BaseView implements View.OnTouchListener {
    @BindView(R.id.app_bar_rv)
    RecyclerView mRecyclerview;
    @BindView(R.id.app_bar_ll)
    LinearLayout mAppBarLL;
    @BindView(R.id.app_bar_pull_in)
    Button mPullIn;
    @BindView(R.id.app_bar_pull_out)
    Button mPullOut;

    private AppAdpater mAppAdpater;
    private AppShowStatus mNowStatus = SHOW;

    private boolean isMove = false;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowManagerParams;
    private Handler mHandler;
    private TranslateAnimation mHideAnimation = new TranslateAnimation(0, 100, 0, 0);
    private TranslateAnimation mShowAnimation = new TranslateAnimation(100, 0, 0, 0);

    public enum AppShowStatus {
        SHOW,
        HIDE
    }

    public AppBar(Context context, WindowManager windowManager, WindowManager.LayoutParams windowManagerParams) {
        super(context);
        mWindowManager = windowManager;
        mWindowManagerParams = windowManagerParams;
    }

    @Override
    protected int getContentView() {
        return R.layout.widget_app_bar;
    }

    @Override
    protected void initView() {
        ArrayList<AppInfoEntity> allAppInfo = AppUtils.getAllAppInfo(mContext);
        mAppAdpater = new AppAdpater(allAppInfo);

        mAppAdpater.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<AppInfoEntity>() {
            @Override
            public void onItemClick(View view, AppInfoEntity appInfoEntity) {
                mContext.startActivity(appInfoEntity.getIntent());
            }
        });
        mRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
        mRecyclerview.setAdapter(mAppAdpater);
        mPullOut.setOnTouchListener(this);
        mHandler = new Handler();
    }

    @OnClick({R.id.app_bar_pull_in, R.id.app_bar_pull_out})
    void appBarPullInOnClick(View view) {
        final int animationTime = 1000;
        switch (mNowStatus) {
            case SHOW:
                mNowStatus = HIDE;
                mAppBarLL.setVisibility(GONE);
                mHideAnimation.setDuration(animationTime);
                mAppBarLL.startAnimation(mHideAnimation);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullOut.setVisibility(VISIBLE);
                        mShowAnimation.setDuration(animationTime);
                        mPullOut.startAnimation(mShowAnimation);
                        updateViewLayout(402);
                    }
                }, animationTime);
                break;
            case HIDE:
                mNowStatus = SHOW;
                mPullOut.setVisibility(GONE);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateViewLayout(0);
                    }
                }, animationTime);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAppBarLL.setVisibility(VISIBLE);
                        mShowAnimation.setDuration(animationTime);
                        mAppBarLL.startAnimation(mShowAnimation);
                    }
                }, animationTime * 2);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {
            case R.id.app_bar_pull_out:
                if (mNowStatus == HIDE) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            isMove = false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            updateViewLayout((int) (event.getRawY() - v.getHeight() / 2));
                            isMove = true;
                            return true;
                        case MotionEvent.ACTION_UP:
                            if (isMove) {
                                return true;
                            } else {
                                return false;
                            }
                    }
                }
                break;
        }
        return false;
    }

    private void updateViewLayout(int y) {
        mWindowManagerParams.y = y;
        mWindowManager.updateViewLayout(this, mWindowManagerParams); // 刷新显示
    }
}
