package com.tapc.platform.ui.activity.settings.user;

import android.content.pm.IPackageDataObserver;
import android.os.RemoteException;
import android.provider.Settings;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.model.BacklightModel;
import com.tapc.platform.model.ConfigModel;
import com.tapc.platform.ui.fragment.BaseFragment;
import com.tapc.platform.utils.AppUtils;
import com.tapc.platform.utils.SoundCtlUtils;

import butterknife.BindView;
import butterknife.OnClick;

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
        try {
            int backlightValue = BacklightModel.getBacklight(mContext);
            mBacklightBar.setProgress(backlightValue);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
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
    }

    private void initVolume() {
        mVolumeBar.setMax(SoundCtlUtils.getInstance().getMaxVolume(mContext));
        mVolumeBar.setProgress(SoundCtlUtils.getInstance().getVolume(mContext));
        mVolumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                SoundCtlUtils.getInstance().setVolume(mContext, value);
                mBacklightValumeTv.setText(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @OnClick(R.id.setting_clear_cache)
    void clearCache() {
        AppUtils.clearAppUserData(mContext, mContext.getPackageName(), new IPackageDataObserver.Stub() {
            @Override
            public void onRemoveCompleted(String s, boolean result) throws RemoteException {
                if (result) {

                } else {

                }
            }
        });
    }
}
