package com.tapc.platform.model.tcp;

public interface SocketListener {
    void onMessage(byte[] dataBuffer);

    void onOpen(boolean isConnect);

    void onClose();

    void onError(String error);
}
