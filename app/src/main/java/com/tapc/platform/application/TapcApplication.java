package com.tapc.platform.application;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.tapc.platform.service.LocalBinder;
import com.tapc.platform.service.StartService;
import com.tapc.platform.utils.IntentUtils;

/**
 * Created by Administrator on 2017/8/21.
 */

public class TapcApplication extends Application {
    //private RefWatcher mRefWatcher;
    private StartService mService;

    @Override
    public void onCreate() {
        super.onCreate();
        //内存泄漏检测工具
//        mRefWatcher = LeakCanary.install(this);
        IntentUtils.bindService(this, StartService.class, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LocalBinder binder = (LocalBinder) service;
                if (name.getClassName().equals(StartService.class.getName())) {
                    mService = (StartService) binder.getService();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }

    public StartService getService() {
        return mService;
    }
}
