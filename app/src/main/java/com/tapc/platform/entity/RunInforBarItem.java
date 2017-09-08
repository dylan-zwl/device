package com.tapc.platform.entity;

/**
 * Created by Administrator on 2017/8/28.
 */

public class RunInforBarItem {
    private int icon;
    private String value;
    private String unit;

    public RunInforBarItem(int icon, Object value, String unit) {
        this.icon = icon;
        this.value = String.valueOf(value);
        this.unit = unit;
    }


    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
