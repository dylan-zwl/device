package com.tapc.platform.model.rfid;

public class RfidCardCmdCode {
	public static final byte REQUEST = 'A';
	public static final byte ANTICOLL = 'B';
	public static final byte HALT = 'D';
	public static final byte MF_ACTIVATE = 'M';
	public static final byte MF_AUTHENT_E2 = 'E';
	public static final byte MF_AUTHENT = 'F';
	public static final byte MF_READ = 'G';
	public static final byte MF_WRITE = 'H';
	public static final byte PICE_AUTO_DETECT = 'N';
	public static final byte PCD_SET_TX = 'I';
}
