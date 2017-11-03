package com.tapc.platform.ui.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.tapc.platform.R;
import com.tapc.platform.application.Config;
import com.tapc.platform.entity.AppInfoEntity;
import com.tapc.platform.entity.BluetoothConnectStatus;
import com.tapc.platform.library.controller.MachineController;
import com.tapc.platform.ui.activity.settings.user.UserSettingActivity;
import com.tapc.platform.ui.adpater.AppAdpater;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.ui.view.BaseSystemView;
import com.tapc.platform.utils.AppUtils;
import com.tapc.platform.utils.NetUtils;
import com.tapc.platform.utils.RxBus;
import com.tapc.platform.utils.SoundCtlUtils;
import com.tapc.platform.utils.WindowManagerUtils;

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

public class AppBar extends BaseSystemView implements View.OnTouchListener {
    @BindView(R.id.app_bar_rv)
    RecyclerView mRecyclerview;
    @BindView(R.id.app_bar_ll)
    LinearLayout mAppBarLL;
    @BindView(R.id.app_bar_pull_in)
    ImageButton mPullIn;
    @BindView(R.id.app_bar_pull_out)
    ImageButton mPullOut;
    @BindView(R.id.app_bar_wifi)
    ImageButton mWifi;
    @BindView(R.id.app_bar_bluetooth)
    ImageButton mBluetooth;
    @BindView(R.id.app_bar_sound)
    ImageButton mSound;
    @BindView(R.id.app_bar_fan)
    ImageButton mFan;

    private AppAdpater mAppAdpater;
    private WindowManager.LayoutParams mWindowManagerParams;
    private Handler mHandler;
    private Animation mHideAnimation;
    private Animation mShowAnimation;

    private boolean isMove = false;
    private float mTouchY;
    private float mShowBottom;

    private AppShowStatus mNowStatus;
    private Disposable mDisposable;

    public enum AppShowStatus {
        SHOW,
        HIDE
    }

    public AppBar(Context context) {
        super(context);
    }

    ArrayList<AppInfoEntity> allAppInfo;

    @Override
    protected int getContentView() {
        return R.layout.widget_app_bar;
    }

    @Override
    protected void initView() {
        super.initView();
        mHandler = new Handler();
        mPullOut.setOnTouchListener(this);
        mShowBottom = 1080 - getResources().getDimension(R.dimen.bottom_bar_h);

        mDisposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> s) throws Exception {
                allAppInfo = AppUtils.getAllAppInfo(mContext, false);
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

        mShowAnimation = AnimationUtils.loadAnimation(mContext, R.anim.push_right_in);
        mHideAnimation = AnimationUtils.loadAnimation(mContext, R.anim.push_right_out);

        mNowStatus = SHOW;
        appBarPullInOnClick();

        setFanShowStatus();
    }

    @Override
    public WindowManager.LayoutParams getLayoutParams() {
        mWindowManagerParams = WindowManagerUtils.getLayoutParams(0, 132, LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT, Gravity.RIGHT | Gravity.TOP);
        return mWindowManagerParams;
    }

    @OnClick(R.id.app_bar_wifi)
    void openWifiSetting() {
        UserSettingActivity.lunch(mContext,"wifi");
    }

    @OnClick(R.id.app_bar_bluetooth)
    void openBtSetting() {
        UserSettingActivity.lunch(mContext,"bluetooth");
    }

    private void setFanShowStatus() {
        if (Config.isFanOpen) {
            mFan.setBackgroundResource(R.drawable.ic_fan_on);
        } else {
            mFan.setBackgroundResource(R.drawable.ic_fan_off);
        }
    }

    @OnClick(R.id.app_bar_fan)
    void fanOnClick() {
        if (Config.isFanOpen) {
            MachineController.getInstance().setFanSpeed(0);
            Config.isFanOpen = false;
        } else {
            MachineController.getInstance().setFanSpeed(1);
            Config.isFanOpen = true;
        }
        setFanShowStatus();
    }

    private void initStatusView() {
        //wifi
        setWifi();
        RxBus.getsInstance().subscribe(this, NetworkInfo.class, new Consumer<NetworkInfo>() {
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
        RxBus.getsInstance().subscribe(this, BluetoothConnectStatus.class, new Consumer<BluetoothConnectStatus>() {
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
    void appBarPullInOnClick() {
        final int animationTime = 1000;
        switch (mNowStatus) {
            case SHOW:
                mNowStatus = HIDE;
                mAppBarLL.setVisibility(INVISIBLE);
                mHideAnimation.setDuration(animationTime);
                mHideAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        updateViewLayout(402);
                        mAppBarLL.setVisibility(GONE);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPullOut.setVisibility(VISIBLE);
                                mShowAnimation.setDuration(animationTime);
                                mPullOut.startAnimation(mShowAnimation);
                            }
                        }, animationTime);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mAppBarLL.startAnimation(mHideAnimation);
                break;
            case HIDE:
                mNowStatus = SHOW;
                mPullOut.setVisibility(INVISIBLE);
                mHideAnimation.setAnimationListener(
                        new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                updateViewLayout(132);
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPullOut.setVisibility(GONE);
                                        mAppBarLL.setVisibility(VISIBLE);
                                        mAppBarLL.startAnimation(mShowAnimation);
                                    }
                                }, animationTime);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        }
                );
                mPullOut.startAnimation(mHideAnimation);
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
                                if (top > (mShowBottom - v.getHeight())) {
                                    top = (int) (mShowBottom - v.getHeight());
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
        SoundCtlUtils.getInstance().openVolume(mContext);
    }


    @Override
    public void onDestroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
        RxBus.getsInstance().unSubscribe(this);
    }

    private class animation extends DefaultItemAnimator {
        @Override
        public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX,
                                     int fromY, int toX, int toY) {
            View view = newHolder.itemView;
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0.2f, 1f);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f);
            animatorSet.setDuration(1000);
            animatorSet.play(scaleX).with(scaleY).with(alpha);
            animatorSet.start();
            return true;
        }

        @Override
        public boolean animateAdd(RecyclerView.ViewHolder holder) {
//            return super.animateAdd(holder);
            View view = holder.itemView;
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0.2f, 1f);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f);
            animatorSet.setDuration(1000);
            animatorSet.play(scaleX).with(scaleY).with(alpha);
            animatorSet.start();
            return true;
        }
    }
}
