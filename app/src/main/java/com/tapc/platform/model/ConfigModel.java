package com.tapc.platform.model;

import android.content.Context;

import com.tapc.platform.utils.PreferenceHelper;

import static com.tapc.platform.utils.PreferenceHelper.readBoolean;

/**
 * Created by Administrator on 2016/11/13.
 */

public class ConfigModel {
    //file
    public static final String SETTING_CONFIG = "setting_config";

    //key
    private static final String PERSON_DETECT_FUNCTION = "person_detect_fuction";
    private static final String ERP = "erp";
    private static final String TEST = "test";
    private static final String RFID = "rfid";
    private static final String LANGUAGE = "language";
    private static final String BACKLIGHT = "backlight";
    private static final String LOCAL_USER = "local_user";
    private static final String DEVICE_ID = "device_id";

    public static boolean getUnmannedRunCheck(Context context, boolean defaults) {
        return readBoolean(context, SETTING_CONFIG, PERSON_DETECT_FUNCTION, defaults);
    }

    public static void setUnmannedRunCheck(Context context, boolean data) {
        PreferenceHelper.write(context, SETTING_CONFIG, PERSON_DETECT_FUNCTION, data);
    }

    public static boolean getErpFunction(Context context, boolean defaults) {
        return PreferenceHelper.readBoolean(context, SETTING_CONFIG, RFID, defaults);
    }

    public static void setErpFunction(Context context, boolean data) {
        PreferenceHelper.write(context, SETTING_CONFIG, RFID, data);
    }

    public static boolean getRfidFunction(Context context, boolean defaults) {
        return PreferenceHelper.readBoolean(context, SETTING_CONFIG, ERP, defaults);
    }

    public static void setRfidFunction(Context context, boolean data) {
        PreferenceHelper.write(context, SETTING_CONFIG, ERP, data);
    }

    public static boolean getOpenTest(Context context, boolean defaults) {
        return readBoolean(context, SETTING_CONFIG, TEST, defaults);
    }

    public static void setOpenTest(Context context, boolean data) {
        PreferenceHelper.write(context, SETTING_CONFIG, TEST, data);
    }

    public static int getLanguage(Context context, int defaults) {
        return PreferenceHelper.readInt(context, SETTING_CONFIG, LANGUAGE, defaults);
    }

    public static void setLanguage(Context context, int data) {
        PreferenceHelper.write(context, SETTING_CONFIG, LANGUAGE, data);
    }

    public static String getLocalUser(Context context) {
        return PreferenceHelper.readString(context, SETTING_CONFIG, LOCAL_USER);
    }

    public static void setLocalUser(Context context, String data) {
        PreferenceHelper.write(context, SETTING_CONFIG, LOCAL_USER, data);
    }

    public static int getBacklight(Context context, int defaults) {
        return PreferenceHelper.readInt(context, SETTING_CONFIG, BACKLIGHT, defaults);
    }

    public static void setBacklight(Context context, int data) {
        PreferenceHelper.write(context, SETTING_CONFIG, BACKLIGHT, data);
    }


    public static String getDeviceId(Context context, String defaults) {
        return PreferenceHelper.readString(context, SETTING_CONFIG, DEVICE_ID, defaults);
    }

    public static void setDeviceId(Context context, String data) {
        PreferenceHelper.write(context, SETTING_CONFIG, DEVICE_ID, data);
    }
}
