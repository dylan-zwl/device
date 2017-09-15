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
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowManagerParams;
    private Handler mHandler;
    private TranslateAnimation mHideAnimation = new TranslateAnimation(0, 100, 0, 0);
    private TranslateAnimation mShowAnimation = new TranslateAnimation(100, 0, 0, 0);

    private boolean isMove = false;
    private float mTouchY;
    private float moveToBottom;

    private AppShowStatus mNowStatus = SHOW;
    private Disposable mDisposable;

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
        mHandler = new Handler();
        mPullOut.setOnTouchListener(this);
        moveToBottom = 1080 - getResources().getDimension(R.dimen.bottom_bar_h);

        mDisposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> s) throws Exception {
                ArrayList<AppInfoEntity> allAppInfo = AppUtils.getAllAppInfo(mContext);
                mAppAdpater = new AppAdpater(allAppInfo);
                mAppAdpater.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<AppInfoEntity>() {
                    @Override
                    public void onItemClick(View view, AppInfoEntity appInfoEntity) {
                        mContext.startActivity(appInfoEntity.getIntent());
                    }
                });
                s.onNext("start_show");
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                mRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager
                        .HORIZONTAL));
                mRecyclerview.setAdapter(mAppAdpater);
                initStatusView();
            }
        });
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
                        updateViewLayout(132);
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
                            if (Math.abs(moveY) > 10) {
                                int top = (int) (event.getRawY() - v.getHeight() / 2);
                                if (top > (moveToBottom - v.getHeight())) {
                                    top = (int) (moveToBottom - v.getHeight());
                                }
                                updateViewLayout(top);
                                isMove = true;
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

    // 刷新显示
    private void updateViewLayout(int y) {
        mWindowManagerParams.y = y;
        mWindowManager.updateViewLayout(this, mWindowManagerParams);
    }

    @OnClick(R.id.app_bar_sound)
    void soundOnClick() {
        SoundCtlUtils.openVolume(mContext);
    }


    @Override
    public void onDestroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
        RxBus.getInstance().unSubscribe(this);
    }
}
