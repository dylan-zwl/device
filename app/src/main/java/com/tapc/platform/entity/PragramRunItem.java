package com.tapc.platform.entity;

import com.tapc.platform.db.IntervalEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/4.
 */

public class PragramRunItem {
    private Type type = Type.COMMON;
    private String mName;
    private List<Float> mBlockList;
    private List<Float> mLineList;

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
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setProgram(List<IntervalEntity> list) {
        if (list != null && list.size() > 0) {
            mBlockList = new ArrayList<>();
            mLineList = new ArrayList<>();
            for (IntervalEntity entity : list) {
                mBlockList.add(entity.getSpeed());
                mLineList.add(entity.getIncline());
            }
        }
    }

    public List<Float> getBlockList() {
        return mBlockList;
    }

    public List<Float> getLineList() {
        return mLineList;
    }

}
