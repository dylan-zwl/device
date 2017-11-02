package com.tapc.platform.model.key;

/**
 * Created by Administrator on 2017/11/2.
 */

public class KeyCode {
    public static final int START = 0x01;
    public static final int STOP = 0x02;
    public static final int RELAX = 0x03;
    public static final int PAUSE = 0x04;
    public static final int START_PAUSE = 0x16;
    public static final int START_STOP = 0x17;

    public static final int SET_SPEED = 0x05;
    public static final int SPEED_INC_KEY = 0x06;
    public static final int SPEED_DEC_KEY = 0x07;

    public static final int SET_INCLINE = 0x08;
    public static final int INCLINE_INC = 0x09;
    public static final int INCLINE_DEC = 0x0a;

    public static final int VOLUME_EN_DIS = 0x18;
    public static final int VOLUME_EN = 0x0b;
    public static final int VOLUME_DIS = 0x0c;
    public static final int VOLUME_INC = 0x0d;
    public static final int VOLUME_DEC = 0x0e;

    public static final int PROGRAM_SET = 0x10;
    public static final int PROGRAM_INC = 0x11;
    public static final int PROGRAM_DEC = 0x12;

    public static final int NUM_MIN = 0x30;
    public static final int NUM_MAX = 0x39;

    public static final int SPEED_MIN = 0x40;
    public static final int SPEED_MAX = 0x59;

    public static final int INCLINE_MIN = 0x80;
    public static final int INCLINE_MAX = 0x99;

    public static final int MENU = 0x0f;
    public static final int HOME = 0X13;
    public static final int BACK = 0X14;
    public static final int FAN = 0X15;

    public static final int MEDIA_PREVIOUS = 0x19;
    public static final int MEDIA_NEXT = 0x20;
    public static final int CLEAR_MAINTENCE = 0x21;
}
