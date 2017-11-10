package com.tapc.platform.ui.activity.settings.user;

import android.provider.Settings;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.application.Config;
import com.tapc.platform.entity.AppInfoEntity;
import com.tapc.platform.model.common.BacklightModel;
import com.tapc.platform.model.common.ConfigModel;
import com.tapc.platform.ui.fragment.BaseFragment;
import com.tapc.platform.ui.widget.AlertDialog;
import com.tapc.platform.utils.AppUtils;
import com.tapc.platform.utils.FileUtils;
import com.tapc.platform.utils.RxjavaUtils;
import com.tapc.platform.utils.SoundCtlUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/10/10.
 */

public class UserDeviceFragment extends BaseFragment {
    @BindView(R.id.setting_backlight_value)
    TextView mBacklightValumeTv;
    @BindView(R.id.setting_volume_value)
    TextView mVolumeValueTv;
    @BindView(R.id.setting_backlight_sbar)
    SeekBar mBacklightBar;
    @BindView(R.id.setting_volume_sbar)
    SeekBar mVolumeBar;

    @Override
    protected int getContentView() {
        return R.layout.fragment_user_device;
    }

    @Override
    protected void initView() {
        super.initView();
        initBacklight();
        initVolume();
    }

    private void initBacklight() {
        mBacklightBar.setMax(255);
        mBacklightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                try {
                    BacklightModel.setBacklight(mContext, value);
                    ConfigModel.setBacklight(mContext, value);
                    mBacklightValumeTv.setText(value);
                } catch (Exception e) {
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        try {
            int backlightValue = BacklightModel.getBacklight(mContext);
            mBacklightBar.setProgress(backlightValue);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initVolume() {
        mVolumeBar.setMax(SoundCtlUtils.getInstance().getMaxVolume(mContext));
        mVolumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                SoundCtlUtils.getInstance().setVolume(mContext, value);
                mVolumeValueTv.setText(String.valueOf(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mVolumeBar.setProgress(SoundCtlUtils.getInstance().getVolume(mContext));
    }


    @OnClick(R.id.setting_clear_cache)
    void clearCache() {
        new AlertDialog(mContext).setMsgText("是否确定?").setOnClickListener(new AlertDialog.Listener() {
            @Override
            public void positiveOnCick() {
                startClear();
            }

            @Override
            public void negativeOnClick() {

            }
        }).show();
    }

    private void startClear() {
        RxjavaUtils.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                List<AppInfoEntity> mlistAppInfo = AppUtils.getAllAppInfo(mContext, false);
                AppUtils.clearAppExit(mContext, mlistAppInfo);
                for (AppInfoEntity app : mlistAppInfo) {
                    AppUtils.clearAppUserData(mContext, app.getPkgName(), null);
                }
                FileUtils.RecursionDeleteFile(new File(Config.MEDIA_FILE));
                e.onNext("show");
                e.onComplete();
            }
        }, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                new AlertDialog(mContext).setMsgText("清除完成").setButtonVisibility(View.GONE).setTimeOut(3000);
            }
        }, bindUntilEvent(FragmentEvent.DESTROY_VIEW));
    }
}
