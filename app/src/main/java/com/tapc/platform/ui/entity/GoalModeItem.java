package com.tapc.platform.ui.entity;

import com.tapc.platform.R;

/**
 * Created by Administrator on 2017/8/25.
 */

public enum GoalModeItem {
    TIME(R.drawable.ic_goal_time, "时间", false),
    DISTANCE(R.drawable.ic_goal_disatance, "距离", false),
    CALORIE(R.drawable.ic_goal_calorie, "卡路里", false),
    HEART(R.drawable.ic_goal_heart, "心率", false);

    private int icon;
    private String name;
    private boolean isChecked;

    GoalModeItem(int icon, String name, boolean isChecked) {
        this.icon = icon;
        this.name = name;
        this.isChecked = isChecked;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}