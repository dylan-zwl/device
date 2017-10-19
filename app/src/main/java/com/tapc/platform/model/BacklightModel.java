package com.tapc.platform.model;

import android.content.Context;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.tapc.platform.utils.PreferenceHelper;

/**
 * Created by Administrator on 2017/10/17.
 */

public class BacklightModel {
    private Context mContext;
    private String mConfigFilenName;

    public BacklightModel(@NonNull Context context, String configFileName) {
        mContext = context;
        mConfigFilenName = configFileName;
    }

    public int getStartDefBacklight() {
        int value = PreferenceHelper.readInt(mContext, mConfigFilenName, "backlight", -1);
        if (value == -1) {
            value = getBacklight();
        }
        return value;
    }

    public void setStartDefBacklight(int value) {
        PreferenceHelper.write(mContext, mConfigFilenName, "backlight", value);
    }

    public int getBacklight() {
        int backlightValue = 0;
        try {
            backlightValue = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return backlightValue;
    }

    public void setBacklight(int backlightValue) {
        try {
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, backlightValue);
            setStartDefBacklight(backlightValue);
        } catch (Exception e) {

        }
    }
}
