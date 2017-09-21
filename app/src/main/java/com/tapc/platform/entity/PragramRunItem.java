package com.tapc.platform.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/9/4.
 */

public class PragramRunItem {
    private Type type = Type.COMMON;
    private String name;
    private List<Integer> list;

    public enum Type {
        COMMON,
        USER_ADD,
        ADD_PROGRAM,
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }
}
