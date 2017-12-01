package com.tapc.platform.model.net.dao;

/**
 * Created by Administrator on 2017/12/1.
 */

public class FaceIdentifyAckResult {
    private String group_id;
    private String uid;
    private String user_info;
    private double[] scores;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser_info() {
        return user_info;
    }

    public void setUser_info(String user_info) {
        this.user_info = user_info;
    }

    public double[] getScores() {
        return scores;
    }

    public void setScores(double[] scores) {
        this.scores = scores;
    }
}
