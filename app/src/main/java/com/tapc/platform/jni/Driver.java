package com.tapc.platform.jni;

import android.app.Instrumentation;
import android.view.KeyEvent;

/**
 * Created by Administrator on 2017/11/2.
 */

public class Driver {
    static {
        System.loadLibrary("driver");
    }

    /**
     * @Description: 模拟键值
     * @param: KEY_EVENT_TYPE =0 ：使用sendKeyDownUpSync  KEY_EVENT_TYPE !=0 ： 使用uinput驱动
     */
    public static int KEY_EVENT_TYPE = 1;
    public static final String UINPUT_DEVICE_NAME = "/dev/uinput";

    public static native int openUinput(String devName);

    public static native void closeUinput();

    private static native void backEvent();

    private static native void homeEvent();

    public static void back() {
        if (KEY_EVENT_TYPE == 0) {
            sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
        } else {
            backEvent();
        }
    }

    public static void home() {
        if (KEY_EVENT_TYPE == 0) {
            sendKeyDownUpSync(KeyEvent.KEYCODE_HOME);
        } else {
            homeEvent();
        }
    }

    public static void sendKeyDownUpSync(final int key) {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * @Description: 初始化串口
     * @param: devName：驱动名字  baudRate：波特率
     */
    public static native int initCom(String devName, int baudRate);
}
