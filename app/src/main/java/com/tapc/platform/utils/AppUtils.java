package com.tapc.platform.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.tapc.platform.entity.AppInfoEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AppUtils {
    private static final String TAG = AppUtils.class.getSimpleName();
    private static List<AppInfoEntity> sAppInforList;
    private static List<String> sNotFiterAppNameList;

    public static List<AppInfoEntity> getAppInforList() {
        return sAppInforList;
    }

    public static ArrayList<AppInfoEntity> getAllAppInfo(Context context, boolean isShowSystemApp) {
        ArrayList<AppInfoEntity> mlistAppInfo = new ArrayList<AppInfoEntity>();
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> appInfos = pm
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo applicationInfo : appInfos) {
            if (applicationInfo != null) {
                boolean isSystemApp = false;
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                    isSystemApp = false;
                } else {
                    isSystemApp = true;
                }

                if (isShowSystemApp == false && isSystemApp) {
                    continue;
                }

                AppInfoEntity appInfo = new AppInfoEntity();
                String pakageName = applicationInfo.packageName;
                String appLabel = (String) applicationInfo.loadLabel(pm);
                Drawable icon = applicationInfo.loadIcon(pm);
                Intent launchIntent = new Intent();
                if (pakageName != null) {
                    launchIntent = context.getPackageManager().getLaunchIntentForPackage(pakageName);
                }
                appInfo.setAppLabel(appLabel);
                appInfo.setPkgName(pakageName);
                appInfo.setAppIcon(icon);
                appInfo.setSystemApp(isSystemApp);
                appInfo.setIntent(launchIntent);
                mlistAppInfo.add(appInfo);
            }
        }
        return mlistAppInfo;
    }

    /**
     * 判断当前应用程序处于前台还是后台
     */
    public static boolean isApplicationBroughtToBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 安装apk
     */
    public static void installApp(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static boolean installApp(Context context, File file, IPackageInstallObserver.Stub observer) {
        try {
            Uri uri = Uri.fromFile(file);
            PackageManager pm = context.getPackageManager();
            pm.installPackage(uri, observer, PackageManager.INSTALL_REPLACE_EXISTING, file.getAbsolutePath());
            return true;
        } catch (Exception e) {
            Log.d(TAG, "install " + file.getName() + " fail");
        }
        return false;
    }

    public static void unInstallApp(Context context, String pkgName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + pkgName));
        context.startActivity(intent);
    }

    public static boolean unInstallApp(Context context, String pkgName, IPackageDeleteObserver.Stub observer) {
        try {
            PackageManager pm = context.getPackageManager();
            pm.deletePackage(pkgName, observer, 0);
            return true;
        } catch (Exception e) {
            Log.d(TAG, "delete " + pkgName + " fail");
        }
        return false;
    }

    public static void clearAppUserData(Context context, String pakageName, IPackageDataObserver.Stub observer) {
        try {
            PackageManager pm = context.getPackageManager();
            pm.clearApplicationUserData(pakageName, observer);
        } catch (Exception e) {
            Log.d(TAG, "clean " + pakageName + " fail");
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }
}
