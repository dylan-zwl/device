package com.tapc.platform.model.net.dao;

/**
 * Created by Administrator on 2017/12/1.
 */

public class FaceIdentifyAck {
    private int result_num;
    private String[] ext_info;
    private FaceIdentifyAckResult[] result;

    private long log_id;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getResult_num() {
        return result_num;
    }

    public void setResult_num(int result_num) {
        this.result_num = result_num;
    }

    public String[] getExt_info() {
        return ext_info;
    }

    public void setExt_info(String[] ext_info) {
        this.ext_info = ext_info;
    }

    public FaceIdentifyAckResult[] getResult() {
        return result;
    }

    public void setResult(FaceIdentifyAckResult[] result) {
        this.result = result;
    }
}
