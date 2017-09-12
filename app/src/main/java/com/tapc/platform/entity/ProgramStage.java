package com.tapc.platform.entity;

/**
 * Created by Administrator on 2017/9/12.
 */

public class ProgramStage {
    private int index;
    private int time;
    private float leftValue;
    private float rightValue;
    private float maxLeftValue;
    private float maxRightValue;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public float getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(float leftValue) {
        this.leftValue = leftValue;
    }

    public float getRightValue() {
        return rightValue;
    }

    public void setRightValue(float rightValue) {
        this.rightValue = rightValue;
    }

    public float getMaxLeftValue() {
        return maxLeftValue;
    }

    public void setMaxLeftValue(float maxLeftValue) {
        this.maxLeftValue = maxLeftValue;
    }

    public float getMaxRightValue() {
        return maxRightValue;
    }

    public void setMaxRightValue(float maxRightValue) {
        this.maxRightValue = maxRightValue;
    }
}
