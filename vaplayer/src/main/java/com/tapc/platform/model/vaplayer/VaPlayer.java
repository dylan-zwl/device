package com.tapc.platform.model.vaplayer;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class VaPlayer {
    public static final String TAG = "VaPlayer";
    private SurfaceHolder mSurfaceHolder;

    private static int mVideoMaxSpeed;
    private static int mVideoMinSpeed;
    private double mCtlMaxSpeed;
    private double mCtlMinSpeed;

    public boolean PlayFlag = false;
    private float mVolume = 1.0f;
    public MediaPlayer mMediaPlayer = null;
    private MediaPlayer mVoicePlayer = null;
    private MediaPlayer mBackMusic = null;

    private int mBackMusicIndex = 0;
    private boolean isCanSetVideo = false;
    private boolean haveBackMusic = false;
    private List<String> mBackMusicList;
    private PlayEntity mNowVaPlayVideo;
    private int mVideoIndex = 0;

    private int mOldIncline = 0;
    private double mVideoSpeed = 0;
    private int mCurrentPosition = -1;

    private int mLevelID = 1;
    private int mVaLanguage = 1;

    private PlayerListener mListener;
    private VoiceThread mVoiceThread = null;

    public VaPlayer(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
        mVideoIndex = 0;
    }

    public interface PlayerListener {
        void setIncline(int videoIncline);

        void setPlaySpeed(MediaPlayer player, int videoSpeed);

        void error(String text);

        void videoMessage(String text);
    }

    public void setPlayerListener(PlayerListener listener) {
        mListener = listener;
    }

    public void initVideoSpeed(int min, int max) {
        mVideoMinSpeed = min;
        mVideoMaxSpeed = max;
    }

    public void initCtlSpeed(double minSpeed, double maxSpeed) {
        mCtlMinSpeed = minSpeed;
        mCtlMaxSpeed = maxSpeed;
    }

    public void init() {
        initBackMusic();
    }

    private void releaseMediaPlayer() {
        if (mVoicePlayer != null) {
            mVoicePlayer.release();
            mVoicePlayer = null;
        }
    }

    private void playVideo(int VideoIndex) {
        try {
            isCanSetVideo = false;
            String EvtFile = mNowVaPlayVideo.getPath() + mNowVaPlayVideo.getEvtList().get(VideoIndex);
            InputStream EvtInputStream = new FileInputStream(EvtFile);
            CvaFactory cvafactory = new CvaFactory();
            CvaVideo video = new CvaVideo();
            String videoDirectory = EvtFile.substring(0, EvtFile.lastIndexOf("/") + 1);
            video.setPath(videoDirectory);
            cvafactory.DeserializeVideo(EvtInputStream,
                    EvtInputStream.available(), video);
            EvtInputStream.close();
            video.setLevel(mLevelID);
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mplayer) {
                        stopVoiceThread();
                        releaseMediaPlayer();
                        mCurrentPosition = -1;
                        if (PlayFlag) {
                            mVideoIndex++;
                            if (mVideoIndex < mNowVaPlayVideo.getEvtList().size()) {
                                playVideo(mVideoIndex);
                            } else {
                                mVideoIndex = 0;
                                playVideo(mVideoIndex);
                            }
                        }
                    }
                });
                mMediaPlayer.setOnErrorListener(new OnErrorListener() {

                    @Override
                    public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                        releaseMediaPlayer();
                        String error = "Play " + mNowVaPlayVideo.getEvtList().get(mVideoIndex) + " error";
                        Log.d("mMediaPlayer", error);
                        if (mListener != null) {
                            mListener.error(error);
                        }
                        return false;
                    }
                });
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.setDataSource(video.getPath() + "/" + video.getFileName());
            Log.d("play video", video.getPath() + video.getFileName());
            mMediaPlayer.setVolume(0.0f, 0.0f);
            mMediaPlayer.prepare();
            if (mCurrentPosition != -1) {
                Log.d("seek to  video CurrentPosition", "" + mCurrentPosition / 1000);
                mMediaPlayer.seekTo(mCurrentPosition);
            }
            mMediaPlayer.start();
            if (haveBackMusic == false) {
                mVoiceThread = new VoiceThread(video);
                mVoiceThread.start();
            }
            isCanSetVideo = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void releaseVideoVoice() {
        if (mVoicePlayer != null) {
            mVoicePlayer.release();
            mVoicePlayer = null;
        }
    }

    private void playVideoVoice(String filePath) {
        try {
            if (mVoicePlayer == null) {
                mVoicePlayer = new MediaPlayer();
                mVoicePlayer.setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        releaseVideoVoice();
                        Log.d(TAG, "Voice play end");
                    }
                });
                mVoicePlayer.setOnErrorListener(new OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mplyer, int arg1, int arg2) {
                        releaseVideoVoice();
                        Log.d(TAG, "Voice play error");
                        return false;
                    }
                });
            }
            mVoicePlayer.reset();
            mVoicePlayer.setDataSource(filePath);
            mVoicePlayer.setVolume(mVolume, mVolume);
            mVoicePlayer.prepare();
            mVoicePlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void releaseBackMusic() {
        if (mBackMusic != null) {
            mBackMusic.release();
            mBackMusic = null;
        }
    }

    private void playBackMusic() {
        try {
            if (haveBackMusic == false || mBackMusicList == null || mBackMusicList.size() == 0) {
                return;
            }
            if (mBackMusic == null) {
                mBackMusic = new MediaPlayer();
                mBackMusic.setOnCompletionListener(new OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer arg0) {
                        if (PlayFlag) {
                            playBackMusic();
                        }
                    }
                });
                mBackMusic.setOnErrorListener(new OnErrorListener() {

                    @Override
                    public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                        if (PlayFlag) {
                            playBackMusic();
                        }
                        return false;
                    }
                });
            }
            mBackMusic.reset();
            if (mBackMusicIndex++ >= mBackMusicList.size()) {
                mBackMusicIndex = 0;
            }
            mBackMusic.setDataSource(mBackMusicList.get(mBackMusicIndex));
            mBackMusic.setVolume(mVolume, mVolume);
            mBackMusic.prepare();
            mBackMusic.start();
            Log.d(TAG, "Back Music next play : " + mBackMusicList.get(mBackMusicIndex));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBackMusic() {
        if (mBackMusicList == null) {
            File filePath = new File(mNowVaPlayVideo.getPath() + "/music");
            File[] Files = filePath.listFiles(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isFile();
                }
            });
            if (Files != null && Files.length > 0) {
                mBackMusicList = new ArrayList<String>();
                for (File file : Files) {
                    mBackMusicList.add(file.getAbsolutePath());
                }
            }
        }
    }

    public void setVolume(float volume) {
        mVolume = volume;
        setPlyerVolume(mVoicePlayer);
        setPlyerVolume(mBackMusic);
    }

    private void setPlyerVolume(MediaPlayer mediaplayer) {
        if (mediaplayer != null) {
            mediaplayer.setVolume(mVolume, mVolume);
        }
    }

    public boolean setLevel(int Level) {
        mLevelID = Level;
        return true;
    }

    public int getLevel() {
        return mLevelID;
    }

    public int getLanguage() {
        return mVaLanguage;
    }

    public void setLanguage(int Language) {
        mVaLanguage = Language;
    }

    private void stopVoiceThread() {
        if (mVoiceThread != null) {
            while (!mVoiceThread.Stop()) ;
            mVoiceThread = null;
        }
    }

    public void start(PlayEntity vaPlay) {
        stopVoiceThread();
        PlayFlag = true;
        mNowVaPlayVideo = vaPlay;
        playVideo(mVideoIndex);
        playBackMusic();
    }

    public void stop() {
        mCurrentPosition = -1;
        mVideoIndex = 0;
        stopVoiceThread();
        releaseMediaPlayer();
        releaseVideoVoice();
        releaseBackMusic();
        PlayFlag = false;
    }

    public boolean isPause() {
        return !PlayFlag;
    }

    public void setPause(boolean isPause) {
        if (PlayFlag == isPause) {
            PlayFlag = !isPause;
            playerPause(mMediaPlayer, PlayFlag);
            playerPause(mVoicePlayer, PlayFlag);
            playerPause(mBackMusic, PlayFlag);
        }
    }

    private void playerPause(MediaPlayer mediaplayer, boolean playFlag) {
        if (mediaplayer != null) {
            if (playFlag) {
                if (!mediaplayer.isPlaying()) {
                    mediaplayer.start();
                }
            } else {
                if (mediaplayer.isPlaying()) {
                    mediaplayer.pause();
                }
            }
        }
    }

    public double getPlaySpeed() {
        return mVideoSpeed;
    }

    public void setPlaySpeed(double speed) {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying() && isCanSetVideo) {
            int videoSpeed = mVideoMinSpeed + (int) (((speed - mCtlMinSpeed) / (mCtlMaxSpeed - mCtlMinSpeed)) *
                    (mVideoMaxSpeed - mVideoMinSpeed));
            if (videoSpeed > mVideoMaxSpeed) {
                videoSpeed = mVideoMaxSpeed;
            } else if (videoSpeed < videoSpeed) {
                videoSpeed = mVideoMinSpeed;
            }
            if (mVideoSpeed != speed) {
                Log.d("videoSpeed : " + videoSpeed, "speed : " + speed + " maxspeed:" + mCtlMaxSpeed + " minspeed:" +
                        mCtlMinSpeed);
                if (mListener != null) {
                    mListener.setPlaySpeed(mMediaPlayer, videoSpeed);
                    Log.d("set video speed ( " + videoSpeed + " ) video CurrentPosition ", "" + mMediaPlayer
                            .getCurrentPosition() / 1000);
                }
                mVideoSpeed = speed;
            }
        }
    }

    public void setBackMusicVisibility(boolean flag) {
        haveBackMusic = flag;
    }

    public VaRecordPosition getPlayPosition() {
        VaRecordPosition vaRecordPosition = new VaRecordPosition();
        if (mMediaPlayer != null) {
            vaRecordPosition.setPlayEntity(mNowVaPlayVideo);
            vaRecordPosition.setVaRecordPosition(true, mVideoIndex, mMediaPlayer.getCurrentPosition());
        }
        return vaRecordPosition;
    }

    public void setPlayPosition(VaRecordPosition vaRecordPosition) {
        if (vaRecordPosition != null) {
            mVideoIndex = vaRecordPosition.getVideoIndex();
            mCurrentPosition = vaRecordPosition.getCurrentPosition();
        } else {
            mVideoIndex = 0;
            mCurrentPosition = -1;
        }
    }

    private class VoiceThread extends Thread {
        private CvaVideo video;
        private String voiceFile;
        private String videomessage;
        private boolean _run = true;
        private boolean endFlag = false;

        public VoiceThread(CvaVideo video) {
            this.video = video;
        }

        public boolean Stop() {
            this._run = false;
            return endFlag;
        }

        @Override
        public void run() {
            if (video.getLevelCount() > 0) {
                if (mCurrentPosition != -1) {
                    SystemClock.sleep(2000);
                }
                int CurrentFrame = (int) (((double) mMediaPlayer
                        .getCurrentPosition() * video.getFramesPerSecondx1000()) / 1000000);
                if (video.findPreviousValueEventIndex(CurrentFrame)) {
                    if (mCurrentPosition != -1) {
                        while (video.searchIndex >= 1) {
                            video.searchIndex = video.searchIndex - 1;
                            if ((video.getFileNameID() != CvaEvent.INVALID_EVENT_VALUE)) {
                                break;
                            }
                        }
                    }
                    // Log.d("FindPreviousValueEventIndex",
                    // "true:" + video.searchIndex + " EventCount: "
                    // + video.getEventCount());
                    while (_run) {
                        if (mMediaPlayer == null && (!mMediaPlayer.isPlaying())) {
                            endFlag = true;
                            return;
                        }
                        CurrentFrame = (int) (((double) mMediaPlayer.getCurrentPosition() * video
                                .getFramesPerSecondx1000()) / 1000000);
                        // Log.d("video CurrentPosition",
                        // "" + mMediaPlayer.getCurrentPosition() / 1000);
                        if (video.findNextEventFrame(CurrentFrame,
                                video.searchIndex)) {
                            // Log.d("FindNextEventFrame", "SearchIndex: "
                            // + video.searchIndex + " CurrentFrame: "
                            // + CurrentFrame + " frame: "
                            // + video.nextFrame);
                            if (video.getResistance() != CvaEvent.INVALID_EVENT_VALUE) {
                            }
                            if (video.getIncline() != CvaEvent.INVALID_EVENT_VALUE) {
                                int videoIncline = video.getIncline();
                                Log.d("videoIncline", "" + videoIncline);
                                if (mOldIncline != videoIncline) {
                                    mOldIncline = videoIncline;
                                    if (mListener != null) {
                                        mListener.setIncline(videoIncline);
                                    }
                                }
                            }
                            if (video.getFileNameID() != CvaEvent.INVALID_EVENT_VALUE) {
                                voiceFile = video.getPath() + video.getEventMessage(mVaLanguage, video.getFileNameID());
                                Log.d("voiceFile", voiceFile);
                                if (new File(voiceFile).exists()) {
                                    playVideoVoice(voiceFile);
                                }
                            }
                            if (video.getMessageID() != CvaEvent.INVALID_EVENT_VALUE) {
                                videomessage = video.getEventMessage(mVaLanguage, video.getMessageID());
                                Log.d("videomessage", videomessage);
                                if (mListener != null) {
                                    mListener.videoMessage(videomessage);
                                }
                            }
                            video.searchIndex++;
                        }
                        SystemClock.sleep(100);
                    }
                } else {
                    Log.d("FindPreviousValueEventIndex", "false:" + video.searchIndex);
                }
            }
            endFlag = true;
            Log.d(TAG, "VoiceThread end");
        }
    }
}
