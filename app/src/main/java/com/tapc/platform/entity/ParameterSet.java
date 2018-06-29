package com.tapc.platform.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/8/31.
 */

public class ParameterSet {
    private String name;
    private String value;
    private String unit;
    private List<Object> defValues;
    private Range range;

    public static class Range {
        public float min;
        public float max;

        public Range(float min, float max) {
            this.min = min;
            this.max = max;
        }
    }

    public ParameterSet(String name, String value, String unit, List<Object> defValues, Range range) {
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.defValues = defValues;
        this.range = range;
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

    public List<Object> getDefValues() {
        return defValues;
    }

    public void setDefValues(List<Object> defValues) {
        this.defValues = defValues;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }
}
