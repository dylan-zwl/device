package com.tapc.platform.ui.widget;

import android.content.Context;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.tapc.platform.R;
import com.tapc.platform.entity.AppInfoEntity;
import com.tapc.platform.entity.BluetoothConnectStatus;
import com.tapc.platform.ui.adpater.AppAdpater;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.utils.AppUtils;
import com.tapc.platform.utils.NetUtils;
import com.tapc.platform.utils.RxBus;
import com.tapc.platform.utils.SoundCtlUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

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
    @BindView(R.id.app_bar_wifi)
    ImageButton mWifi;
    @BindView(R.id.app_bar_bluetooth)
    ImageButton mBluetooth;
    @BindView(R.id.app_bar_sound)
    ImageButton mSound;
    @BindView(R.id.app_bar_fan)
    ImageButton mFan;

    private AppAdpater mAppAdpater;
    private AppShowStatus mNowStatus = SHOW;

    private boolean isMove = false;
    private float mTouchY;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowManagerParams;
    private Handler mHandler;
    private TranslateAnimation mHideAnimation = new TranslateAnimation(0, 100, 0, 0);
    private TranslateAnimation mShowAnimation = new TranslateAnimation(100, 0, 0, 0);

    private float moveToBottom;

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
        moveToBottom = 1080 - getResources().getDimension(R.dimen.bottom_bar_h);
        initStatusView();

    }

    private void initStatusView() {
        //wifi
        setWifi();
        RxBus.getInstance().subscribe(this, NetworkInfo.class, new Consumer<NetworkInfo>() {
            @Override
            public void accept(@NonNull NetworkInfo networkInfo) throws Exception {
                setWifi();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
            }
        });
        //bluetooth
        RxBus.getInstance().subscribe(this, BluetoothConnectStatus.class, new Consumer<BluetoothConnectStatus>() {
            @Override
            public void accept(@NonNull BluetoothConnectStatus status) throws Exception {
                if (status.isConnected()) {
                    mBluetooth.setBackgroundResource(R.drawable.ic_bt_on);
                } else {
                    mBluetooth.setBackgroundResource(R.drawable.ic_bt_off);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
            }
        });
    }

    private void setWifi() {
        if (NetUtils.isConnected(mContext)) {
            mWifi.setBackgroundResource(R.drawable.ic_wifi_on);
        } else {
            mWifi.setBackgroundResource(R.drawable.ic_wifi_off);
        }
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
                            mTouchY = event.getRawY();
                            isMove = false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float moveY = mTouchY - event.getRawY();
                            if (moveY > 10) {
                                isMove = true;
                                float top = event.getRawY() - v.getHeight() / 2;
                                if (top > (moveToBottom - v.getHeight())) {
                                    top = moveToBottom - v.getHeight();
                                }
                                updateViewLayout((int) top);
                            } else {
                                isMove = false;
                            }
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

    @OnClick(R.id.app_bar_sound)
    void soundOnClick() {
        SoundCtlUtils.openVolume(mContext);
    }


    @Override
    public void onDestroy() {
        RxBus.getInstance().unSubscribe(this);
    }
}
