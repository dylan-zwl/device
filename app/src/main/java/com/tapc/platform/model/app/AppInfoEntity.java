package com.tapc.platform.model.app;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class AppInfoEntity {
    private String appLabel;
    private Drawable appIcon;
    private Intent intent;
    private String pkgName;
    private int type;
    private boolean systemApp;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public AppInfoEntity() {
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public void setSystemApp(boolean systemApp) {
        this.systemApp = systemApp;
    }

    public boolean isSystemApp() {
        return systemApp;
    }
}
