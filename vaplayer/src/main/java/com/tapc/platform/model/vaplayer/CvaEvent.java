package com.tapc.platform.model.vaplayer;

public class CvaEvent {
    public static int INVALID_EVENT_VALUE = 0x8FFFFFFF;
    private int mFrame;
    private int mIncline;
    private int mResistance;
    private int mMessageID;
    private int mFileNameID;

    public CvaEvent() {
        mFrame = INVALID_EVENT_VALUE;
        mIncline = INVALID_EVENT_VALUE;
        mResistance = INVALID_EVENT_VALUE;
        mMessageID = INVALID_EVENT_VALUE;
        mFileNameID = INVALID_EVENT_VALUE;
    }

    public CvaEvent(CvaEvent Event) {
        mFrame = Event.mFrame;
        mIncline = Event.mIncline;
        mResistance = Event.mResistance;
        mMessageID = Event.mMessageID;
        mFileNameID = Event.mFileNameID;
    }

    public int GetFrame() {
        return mFrame;
    }

    public void SetFrame(int Frame) {
        mFrame = Frame;
    }

    public int GetIncline() {
        return mIncline;
    }

    public void SetIncline(int Incline) {
        mIncline = Incline;
    }

    public int GetResistance() {
        return mResistance;
    }

    public void SetResistance(int Resistance) {
        mResistance = Resistance;
    }

    public int GetMessageID() {
        return mMessageID;
    }

    public void SetMessageID(int MessageID) {
        mMessageID = MessageID;
    }

    public int GetFileNameID() {
        return mFileNameID;
    }

    public void SetFileNameID(int FileNameID) {
        mFileNameID = FileNameID;
    }
}
