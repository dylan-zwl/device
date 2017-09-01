package com.tapc.platform.service;

import android.app.Service;
import android.os.Binder;

/**
 * Created by Administrator on 2017/8/25.
 */

public class LocalBinder extends Binder {
    private Service mService;

    public LocalBinder(Service service) {
        this.mService = service;
    }

    public Service getService() {
        return mService;
    }

    public void setService(Service service) {
        this.mService = service;
    }
}
