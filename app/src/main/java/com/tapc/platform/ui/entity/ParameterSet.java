package com.tapc.platform.ui.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/8/31.
 */

public class ParameterSet {
    private String name;
    private String value;
    private String unit;
    private List<Object> defValues;

    public ParameterSet(String name, String value, String unit, List<Object> defValues) {
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.defValues = defValues;
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
}
