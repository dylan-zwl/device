package com.tapc.platform.model.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;


public class AppModel {
    private static final String TAG = AppModel.class.getSimpleName();
    private static List<AppInfoEntity> sAppInforList;
    private static List<String> sNotFiterAppNameList;

    public static List<AppInfoEntity> getAppInforList() {
        return sAppInforList;
    }


    /**
     * 获取安装应用列表
     */
    public static ArrayList<AppInfoEntity> getAllAppInfo(Context context, boolean isShowSystemApp) {
        ArrayList<AppInfoEntity> mlistAppInfo = new ArrayList<AppInfoEntity>();
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> appInfos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo applicationInfo : appInfos) {
            if (applicationInfo != null) {
                boolean isSystemApp = false;
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                    isSystemApp = false;
                } else {
                    isSystemApp = true;
                }

                if (isShowSystemApp == false && isSystemApp && !applicationInfo.packageName.equals("com.actions" +
                        ".owlplayer") && !applicationInfo.packageName.equals("com.android.music")) {
                    continue;
                }
                String pakageName = applicationInfo.packageName;

                if (pakageName.equals(context.getPackageName())) {
                    continue;
                }

                AppInfoEntity appInfo = new AppInfoEntity();
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
}
