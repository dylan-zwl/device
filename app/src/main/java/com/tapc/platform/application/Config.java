package com.tapc.platform.application;

import com.tapc.platform.entity.DeviceType;

/**
 * Created by Administrator on 2017/9/20.
 */

public class Config {
    public static DeviceType DEVICE_TYPE = DeviceType.TREADMILL;
    public static boolean isFanOpen = false;

    public static final String SETTING_FILE_NAME = "setting";

    public static final String MEDIA_FILE = "/sdcard/tapc";
}
