package com.tapc.platform.model.common;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by Administrator on 2017/10/17.
 */

public class BacklightModel {
    public static int getBacklight(Context context) throws Settings.SettingNotFoundException {
        int backlightValue = 0;
        backlightValue = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        return backlightValue;
    }

    public static void setBacklight(Context context, int backlightValue) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, backlightValue);
    }
}
