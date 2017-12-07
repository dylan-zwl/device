package com.tapc.platform.application;

import com.tapc.platform.entity.DeviceType;

/**
 * Created by Administrator on 2017/9/20.
 */

public class Config {
    public static DeviceType DEVICE_TYPE = DeviceType.TREADMILL;

    public static final String MEDIA_FILE = "/sdcard/tapc";

    public class Debug {
        public static final boolean OPEN_REF_WATCHER = false;
    }

    public class Face {
        public static final String appName = "cloud-face-face-android";
        public static final String licenseFileName = "idl-license.face-android";
    }
}
