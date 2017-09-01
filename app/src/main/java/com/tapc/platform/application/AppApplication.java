package com.tapc.platform.application;

import android.app.Application;

import com.tapc.platform.service.StartService;
import com.tapc.platform.utils.IntentUtils;

/**
 * Created by Administrator on 2017/8/21.
 */

public class AppApplication extends Application {
    //private RefWatcher mRefWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        //内存泄漏检测工具
//        mRefWatcher = LeakCanary.install(this);
        IntentUtils.startService(this, StartService.class);
    }
}
