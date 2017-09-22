package com.tapc.platform.entity;

/**
 * Created by Administrator on 2017/8/25.
 */

public class WorkoutInforItem {
    private WorkoutInforType type;
    private int icon;
    private String name;
    private String value;
    private String unit;

    public WorkoutInforItem(WorkoutInforType type, int icon, String name, String value, String unit) {
        this.type = type;
        this.icon = icon;
        this.name = name;
        this.value = value;
        this.unit = unit;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public WorkoutInforType getType() {
        return type;
    }

    public void setType(WorkoutInforType type) {
        this.type = type;
    }
}