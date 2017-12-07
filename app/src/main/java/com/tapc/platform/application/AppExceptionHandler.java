package com.tapc.platform.application;

import android.util.Log;

import java.lang.Thread.UncaughtExceptionHandler;


public class AppExceptionHandler implements UncaughtExceptionHandler {

    private UncaughtExceptionHandler mDefaultUEH;

    public AppExceptionHandler() {
        this.mDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.d("AppExceptionHandler", "app error exit");
        TapcApplication.getInstance().reload();
        // defaultUEH.uncaughtException(thread, ex);
    }

}
