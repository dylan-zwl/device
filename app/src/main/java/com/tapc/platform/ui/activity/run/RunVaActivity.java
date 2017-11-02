package com.tapc.platform.ui.activity.run;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.tapc.platform.R;
import com.tapc.platform.library.common.AppSettings;
import com.tapc.platform.library.common.CommonEnum;
import com.tapc.platform.library.util.WorkoutEnum.WorkoutUpdate;
import com.tapc.platform.library.workouting.WorkOuting;
import com.tapc.platform.model.vaplayer.PlayEntity;
import com.tapc.platform.model.vaplayer.VaPlayer;
import com.tapc.platform.model.vaplayer.VaRecordPosition;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/9/8.
 */

public class RunVaActivity extends RunBaseActivity {
    @BindView(R.id.va_surface_view)
    SurfaceView mSurfaceView;

    private VaPlayer mPlayer;
    private Disposable mDisposable;
    private PlayEntity mPlayEntity;
    private VaRecordPosition mVaRecordPosition;

    public static void launch(Context c, PlayEntity entity) {
        Intent i = new Intent(c, RunVaActivity.class);
        i.putExtra("play_entity", entity);
        c.startActivity(i);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_va;
    }

    @Override
    protected void initView() {
        super.initView();
        mTapcApp.setHomeActivity(this.getClass());
        initPlayEntity();
        startPlay();
    }

    private void initPlayEntity() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPlayEntity = (PlayEntity) bundle.get("play_entity");
        }
    }

    private void startPlay() {
        WorkOuting.getInstance().subscribeObserverNotification(this);

        if (mPlayEntity == null) {
            return;
        }
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                                      @Override
                                      public void surfaceCreated(SurfaceHolder holder) {
                                          mPlayer = new VaPlayer(mSurfaceView.getHolder());
                                          mPlayer.init();
                                          mPlayer.setBackMusicVisibility(false);
                                          mPlayer.setPlayPosition(mVaRecordPosition);
                                          mPlayer.start(mPlayEntity);
                                      }

                                      @Override
                                      public void surfaceChanged(SurfaceHolder holder, int format, int width, int
                                              height) {

                                      }

                                      @Override
                                      public void surfaceDestroyed(SurfaceHolder holder) {

                                      }
                                  }
        );

//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                SystemClock.sleep(2000);
//                while (!RunVaActivity.this.isDestroyed() && mPlayer != null) {
//                    if (mSurfaceView.isShown()) {
//                        SystemClock.sleep(1000);
//                        e.onNext("set_video_speed");
//                        e.onNext("set_play_status");
//                    }
//                }
//                e.onComplete();
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                mDisposable = d;
//            }
//
//            @Override
//            public void onNext(String cmd) {
//                switch (cmd) {
//                    case "start":
//                        break;
//                    case "set_video_speed":
//                        break;
//                    case "set_play_status":
//                        break;
//                    case "resume_play":
//                        break;
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
    }

    @Override
    protected void update(WorkoutUpdate workoutUpdate) {
        switch (workoutUpdate) {
            case UI_STOP:
                finish();
                break;
            case UI_PAUSE:
                setPlayPause(true);
                break;
            case UI_RESUME:
                setPlayPause(false);
                break;
            case UI_LEFT:
                if (AppSettings.getPlatform() == CommonEnum.Platform.TCC8935) {
                    mPlayer.initVideoSpeed(100000, 200000);
                } else {
                    mPlayer.initVideoSpeed(100, 200);
                }
                break;
        }
    }

    private void setPlayPause(boolean isPause) {
        if (mPlayer != null) {
            mPlayer.setPause(isPause);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mVaRecordPosition = mPlayer.getPlayPosition();
            mPlayer.stop();
            mPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WorkOuting.getInstance().unsubscribeObserverNotification(this);
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }
}
