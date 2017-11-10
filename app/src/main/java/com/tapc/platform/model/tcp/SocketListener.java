package com.tapc.platform.model.tcp;

import java.io.IOException;

public interface SocketListener {
    void onMessage(byte[] dataBuffer);

    void onError(IOException error);
}
