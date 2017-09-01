package com.tapc.platform.model.vaplayer;

public class VaRecordPosition {
    private boolean mNeedToResume = false;
    private int mVideoIndex = 0;
    private int mCurrentPosition = -1;
    private PlayEntity mPlayEntity;

    public void setVaRecordPosition(boolean needToResume, int videoIndex, int currentPosition) {
        this.mNeedToResume = needToResume;
        this.mVideoIndex = videoIndex;
        this.mCurrentPosition = currentPosition;
    }

    public boolean isNeedToResume() {
        return mNeedToResume;
    }

    public void setNeedToResume(boolean needToResume) {
        this.mNeedToResume = needToResume;
    }

    public int getVideoIndex() {
        return mVideoIndex;
    }

    public void setVideoIndex(int videoIndex) {
        this.mVideoIndex = videoIndex;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.mCurrentPosition = currentPosition;
    }

    public PlayEntity getPlayEntity() {
        return mPlayEntity;
    }

    public void setPlayEntity(PlayEntity playEntity) {
        this.mPlayEntity = playEntity;
    }
}
