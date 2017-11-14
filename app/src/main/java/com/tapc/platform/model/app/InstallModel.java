package com.tapc.platform.model.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.util.Log;

import com.tapc.platform.utils.AppUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/23.
 */

public class InstallModel {
    private Context mContext;
    private Listener mListener;

    public InstallModel(Context context) {
        mContext = context;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public interface Listener {
        void completed(AppSettingItem item, String s, int i);
    }

    public void installApp(final AppSettingItem item) {
        final Object lock = new Object();
        boolean result = AppUtils.installApp(mContext, new File(item.getPath()), new IPackageInstallObserver.Stub() {
            @Override
            public void packageInstalled(String s, int i) throws RemoteException {
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


    public List<AppSettingItem> getFiles(String Path, String filter) {
        List<AppSettingItem> items = new ArrayList<>();
        File[] files = new File(Path).listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (f.isFile()) {
                    if (f.getName().contains(filter)) {
                        AppSettingItem item = getAppList(f.getPath());
                        if (item != null) {
                            items.add(item);
                        }
                    }
                }
            }
        }
        return items;
    }

    private AppSettingItem getAppList(String apkPath) {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            AppSettingItem item = new AppSettingItem();
            // 得到安装包名称
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            item.setPath(apkPath);
            if (AppUtils.isAppInstalled(mContext, appInfo.packageName)) {
                item.setStatus(1);
            } else {
                item.setStatus(0);
            }
            try {
                item.setLabel(appInfo.loadLabel(pm).toString());
                item.setIcon(appInfo.loadIcon(pm));
            } catch (OutOfMemoryError e) {
                Log.e("ApkIconLoader", e.toString());
            }
            Log.d("app file", "" + item.getLabel());
            return item;
        }
        return null;
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
