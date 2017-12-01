package com.tapc.platform.model.net.dao;

/**
 * Created by Administrator on 2017/12/1.
 */

public class FaceUserAddAck {
    private long log_id;
    private long error_code;
    private String error_msg;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public long getError_code() {
        return error_code;
    }

    public void setError_code(long error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
