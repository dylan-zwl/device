package com.tapc.platform.model.app;

import android.content.Context;
import android.content.pm.IPackageDeleteObserver;
import android.os.RemoteException;

import com.tapc.platform.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/23.
 */

public class UninstallModel {
    private Context mContext;
    private Listener mListener;

    public UninstallModel(Context context) {
        mContext = context;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public interface Listener {
        void completed(AppSettingItem item, String s, int i);
    }

    public void uninstallApp(final AppSettingItem item) {
        final Object lock = new Object();
        boolean result = AppUtils.unInstallApp(mContext, item.getPkgName(), new IPackageDeleteObserver.Stub() {
            @Override
            public void packageDeleted(String s, int i) throws RemoteException {
                if (mListener != null) {
                    mListener.completed(item, s, i);
                }
                unlock(lock);
            }
        });
        if (result) {
            lock(lock);
        }
    }

    public List<AppSettingItem> getFiles() {
        ArrayList<AppInfoEntity> allLstAppInfo = AppModel.getAllAppInfo(mContext, false);
        List<AppSettingItem> items = new ArrayList<>();
        if (allLstAppInfo != null) {
            for (AppInfoEntity entity : allLstAppInfo) {
                AppSettingItem appItem = new AppSettingItem();
                appItem.setPkgName(entity.getPkgName());
                appItem.setIcon(entity.getAppIcon());
                appItem.setLabel(entity.getAppLabel());
                items.add(appItem);
            }
        }
        return items;
    }

    boolean isFinish = false;

    private void unlock(Object o) {
        isFinish = true;
        synchronized (o) {
            o.notifyAll();
        }
    }

    private void lock(Object o) {
        if (!isFinish) {
            synchronized (o) {
                try {
                    isFinish = false;
                    o.wait();
                    isFinish = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
