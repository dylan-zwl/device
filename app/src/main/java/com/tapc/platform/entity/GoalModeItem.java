package com.tapc.platform.entity;

import com.tapc.platform.R;
import com.tapc.platform.library.util.WorkoutEnum.ProgramType;

/**
 * Created by Administrator on 2017/8/25.
 */

public enum GoalModeItem {
    TIME(ProgramType.TIME, R.drawable.ic_goal_time, R.string.time, false),
    DISTANCE(ProgramType.DISTANCE, R.drawable.ic_goal_disatance, R.string.distance, false),
    CALORIE(ProgramType.CALORIE, R.drawable.ic_goal_calorie, R.string.calorie, false),
    HEART(ProgramType.TIME, R.drawable.ic_goal_heart, R.string.heart_rate, false);

    private ProgramType programType;
    private int icon;
    private int name;
    private boolean isChecked;

    GoalModeItem(ProgramType programType, int icon, int name, boolean isChecked) {
        this.programType = programType;
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


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public ProgramType getProgramType() {
        return programType;
    }

    public void setProgramType(ProgramType programType) {
        this.programType = programType;
    }
}