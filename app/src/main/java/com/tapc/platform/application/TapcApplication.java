package com.tapc.platform.application;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.tapc.platform.entity.DeviceType;
import com.tapc.platform.jni.Driver;
import com.tapc.platform.library.abstractset.ProgramSetting;
import com.tapc.platform.library.common.AppSettings;
import com.tapc.platform.library.common.BikeSystemSettings;
import com.tapc.platform.library.common.Common;
import com.tapc.platform.library.common.SystemSettings;
import com.tapc.platform.library.common.TreadmillSystemSettings;
import com.tapc.platform.library.controller.MachineController;
import com.tapc.platform.library.workouting.WorkOuting;
import com.tapc.platform.model.common.ConfigModel;
import com.tapc.platform.service.LocalBinder;
import com.tapc.platform.service.StartService;
import com.tapc.platform.utils.IntentUtils;
import com.tapc.platform.utils.NetUtils;

/**
 * Created by Administrator on 2017/8/21.
 */

public class TapcApplication extends Application {
    //    private RefWatcher mRefWatcher;
    private static TapcApplication mInstance;
    private StartService mService;
    private KeyEvent mKeyEvent;
    private Class<?> mHomeActivity;
    private ProgramSetting mProgramSetting;

    @Override
    public void onCreate() {
        super.onCreate();
//        内存泄漏检测工具
        if (Config.Debug.OPEN_REF_WATCHER) {
//            mRefWatcher = LeakCanary.install(this);
        }
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

        initDriver();

        initControl();
        initDeviceId();
    }

    private void initDeviceId() {
        String id = NetUtils.getLocalMacAddress(this);
        if (!TextUtils.isEmpty(id)) {
            ConfigModel.setDeviceId(this, id);
        }
    }


    /**
     * 初始化串口驱动，uinput驱动
     */
    private void initDriver() {
        Driver.KEY_EVENT_TYPE = 0;
        Driver.openUinput(Driver.UINPUT_DEVICE_NAME);
        Driver.initCom(Common.makeSerialPort(Build.DEVICE), 115200);
    }

    private void initControl() {
        SystemSettings systemSettings = null;
        if (Config.DEVICE_TYPE == DeviceType.TREADMILL) {
            TreadmillSystemSettings.MAX_INCLINE = 15;
            TreadmillSystemSettings.MIN_INCLINE = -3;
            systemSettings = new TreadmillSystemSettings();
        } else if (Config.DEVICE_TYPE == DeviceType.BIKE) {
            systemSettings = new BikeSystemSettings();
        }
        systemSettings.Load(getApplicationContext(), null);

        //初始下控控制平台
        AppSettings.setSerialPort(Common.makeSerialPort(Build.DEVICE));
        //初始下控控制模式，true 为自动模式， false 为普通模式
        AppSettings.setLoopbackMode(false);

        //初始下控控制
        MachineController controller = MachineController.getInstance();
        controller.initController(this);
        controller.start();

        WorkOuting.getInstance().initWorkOuting(controller.getMachineOperateListener(), this);
    }

//    public void addRefWatcher(Object watchedReference) {
//        if (mRefWatcher != null) {
//            mRefWatcher.watch(watchedReference);
//        }
//    }
//
//    public RefWatcher getRefWatcher() {
//        return mRefWatcher;
//    }

    public static TapcApplication getInstance() {
        return mInstance;
    }

    public StartService getService() {
        return mService;
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

    public void reload() {
        IntentUtils.stopService(this, StartService.class);
        MachineController.getInstance().stop();
        System.exit(1);
    }
}
