package com.tapc.platform.ui.activity.run;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.SurfaceView;

import com.tapc.platform.R;
import com.tapc.platform.entity.WidgetShowStatus;
import com.tapc.platform.model.vaplayer.PlayEntity;
import com.tapc.platform.model.vaplayer.VaPlayer;
import com.tapc.platform.ui.activity.BaseActivity;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/8.
 */

public class RunVaActivity extends BaseActivity {
    @BindView(R.id.va_surface_view)
    SurfaceView mSurfaceView;

    private VaPlayer mPlayer;

    public static void launch(Context c, PlayEntity entity) {
        Intent i = new Intent(c, RunVaActivity.class);
        i.putExtra("sceneEntity", entity);
        c.startActivity(i);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_va;
    }

    @Override
    protected void initView() {
        mTapcApp.setHomeActivity(this.getClass());
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                SystemClock.sleep(1000);
                e.onNext("start");
                SystemClock.sleep(2000);
                while (!RunVaActivity.this.isDestroyed() && mPlayer != null) {
                    if (mSurfaceView.isShown()) {
                        SystemClock.sleep(1000);
                        e.onNext("set_video_speed");
                        e.onNext("set_play_status");
                    }
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String cmd) {
                switch (cmd) {
                    case "start":
                        startPlay();
                        break;
                    case "set_video_speed":

                        break;
                    case "set_play_status":

                        break;
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void startPlay() {
        Bundle bundle = getIntent().getExtras();
        PlayEntity playEntity = null;
        if (bundle != null) {
            playEntity = (PlayEntity) bundle.get("sceneEntity");
        }
        mPlayer = new VaPlayer(mSurfaceView.getHolder());
        mPlayer.init();
//        if (Config.BOARD_TYPE == Config.BoardType.TCC8935) {
//            mPlayer.initVideoSpeed(100000, 200000);
//        } else {
//            mPlayer.initVideoSpeed(100, 200);
//        }
        mPlayer.setBackMusicVisibility(false);
        mPlayer.start(playEntity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTapcApp.getService().setRunInforBarVisibility(WidgetShowStatus.VISIBLE);
        mTapcApp.getService().setAppBarVisibility(WidgetShowStatus.VISIBLE);
    }
}
