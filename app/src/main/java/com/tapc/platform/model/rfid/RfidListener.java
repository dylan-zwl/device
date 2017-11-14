package com.tapc.platform.model.rfid;

public interface RfidListener {
	public void detectCard(byte[] uid);
}
