package com.tapc.platform.model.net.dao;

/**
 * Created by Administrator on 2017/12/1.
 */

public class FaceIdentify {
    private String group_id;
    private String image;
    private String[] ext_fields;
    private int user_top_num = 1;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String[] getExt_fields() {
        return ext_fields;
    }

    public void setExt_fields(String[] ext_fields) {
        this.ext_fields = ext_fields;
    }

    public int getUser_top_num() {
        return user_top_num;
    }

    public void setUser_top_num(int user_top_num) {
        this.user_top_num = user_top_num;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }
}
