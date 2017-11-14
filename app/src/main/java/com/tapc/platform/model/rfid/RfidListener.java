package com.tapc.platform.model.rfid;

public interface RfidListener {
    void detectCard(byte[] uid);
}
