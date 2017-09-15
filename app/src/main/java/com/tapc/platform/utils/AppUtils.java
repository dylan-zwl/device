package com.tapc.platform.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.tapc.platform.entity.AppInfoEntity;

import java.util.ArrayList;
import java.util.List;


public class AppUtils {
    private static List<AppInfoEntity> sAppInforList;
    private static List<String> sNotFiterAppNameList;

    public static List<AppInfoEntity> getAppInforList() {
        return sAppInforList;
    }

    public static ArrayList<AppInfoEntity> getAllAppInfo(Context context) {
        ArrayList<AppInfoEntity> mlistAppInfo = new ArrayList<AppInfoEntity>();
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> appInfos = pm
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo applicationInfo : appInfos) {
            if (applicationInfo != null) {
                String pakageName = applicationInfo.packageName;
                String appLabel = (String) applicationInfo.loadLabel(pm);
                Drawable icon = applicationInfo.loadIcon(pm);
                Intent launchIntent = new Intent();
                if (pakageName != null) {
                    launchIntent = context.getPackageManager().getLaunchIntentForPackage(pakageName);
                }
                AppInfoEntity appInfo = new AppInfoEntity();
                if ((!pakageName.equals(context.getPackageName()) && (applicationInfo.flags & ApplicationInfo
                        .FLAG_SYSTEM) <= 0) || sNotFiterAppNameList !=
                        null) {
                    if (sNotFiterAppNameList != null && !sNotFiterAppNameList.isEmpty()) {
                        for (String notFiterApp : sNotFiterAppNameList) {
                            if (pakageName.equals(notFiterApp)) {
                                appInfo.setAppLabel(appLabel);
                                appInfo.setPkgName(pakageName);
                                appInfo.setAppIcon(icon);
                                appInfo.setIntent(launchIntent);
                                mlistAppInfo.add(appInfo);
                                break;
                            }
                        }
                    } else {
                        appInfo.setAppLabel(appLabel);
                        appInfo.setPkgName(pakageName);
                        appInfo.setAppIcon(icon);
                        appInfo.setIntent(launchIntent);
                        mlistAppInfo.add(appInfo);
                    }
                }
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
}
