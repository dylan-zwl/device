package com.tapc.platform.entity;

/**
 * Created by Administrator on 2017/10/20.
 */

public class CtlParameterItem {
    private String name;
    private String value;

    public CtlParameterItem(String name, String value) {
        this.name = name;
        this.value = value;
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
}
