package com.tapc.platform.application;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.jht.tapc.jni.KeyEvent;
import com.tapc.platform.entity.DeviceType;
import com.tapc.platform.library.abstractset.ProgramSetting;
import com.tapc.platform.library.common.AppSettings;
import com.tapc.platform.library.common.BikeSystemSettings;
import com.tapc.platform.library.common.CommonEnum;
import com.tapc.platform.library.common.SystemSettings;
import com.tapc.platform.library.common.TreadmillSystemSettings;
import com.tapc.platform.library.controller.MachineController;
import com.tapc.platform.library.workouting.WorkOuting;
import com.tapc.platform.service.LocalBinder;
import com.tapc.platform.service.StartService;
import com.tapc.platform.utils.IntentUtils;

/**
 * Created by Administrator on 2017/8/21.
 */

public class TapcApplication extends Application {
    //private RefWatcher mRefWatcher;
    private static TapcApplication mInstance;
    private StartService mService;
    private KeyEvent mKeyEvent;
    private Class<?> mHomeActivity;
    private ProgramSetting mProgramSetting;

    @Override
    public void onCreate() {
        super.onCreate();
        //内存泄漏检测工具
//        mRefWatcher = LeakCanary.install(this);
        mInstance = this;
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

//        mKeyEvent = new KeyEvent(null, 0);
//        mKeyEvent.initCom();
        initControl(this);
    }

    private void initControl(Context context) {
        SystemSettings systemSettings = null;
        if (Config.DEVICE_TYPE == DeviceType.TREADMILL) {
            systemSettings = new TreadmillSystemSettings();
        } else if (Config.DEVICE_TYPE == DeviceType.BIKE) {
            systemSettings = new BikeSystemSettings();
        }
        if (systemSettings != null) {
            systemSettings.Load(this, null);
            systemSettings.mPath = "/mnt/sdcard/premierprograms.db";
            AppSettings.setPlatform(CommonEnum.Platform.S700);
            AppSettings.setLoopbackMode(false);
            MachineController controller = MachineController.getInstance();
            controller.initController(this);
            controller.start();
            WorkOuting.getInstance().initWorkOuting(controller.getMachineOperateListener(), this);
        }
    }

    public static TapcApplication getInstance() {
        return mInstance;
    }

    public StartService getService() {
        return mService;
    }

    public KeyEvent getKeyEvent() {
        return mKeyEvent;
    }

    public Class<?> getHomeActivity() {
        return mHomeActivity;
    }

    public void setHomeActivity(Class<?> homeActivity) {
        this.mHomeActivity = homeActivity;
    }

    public ProgramSetting getProgramSetting() {
        return mProgramSetting;
    }

    public void setProgramSetting(ProgramSetting programSetting) {
        this.mProgramSetting = programSetting;
    }
}
