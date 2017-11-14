package com.tapc.platform.model.rfid;

/**
 * Created by Administrator on 2017/11/13.
 */

public interface RfidAdapter {
    void setRecvListerner(RecvListener recvListener);

    void sendData(int length, byte[] datas);

    void setReadStatus(int readStatus);
}
