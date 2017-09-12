package com.tapc.platform.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by Administrator on 2016/11/13.
 */
public class SoundCtlUtils {
    private static SoundCtlUtils sSoundCtlUtils;

    public static SoundCtlUtils getInstance() {
        if (sSoundCtlUtils == null) {
            synchronized (SoundCtlUtils.class) {
                if (sSoundCtlUtils == null) {
                    sSoundCtlUtils = new SoundCtlUtils();
                }
            }
        }
        return sSoundCtlUtils;
    }

    public static void openVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_SAME, AudioManager
                .FLAG_SHOW_UI);
    }

    public static void addVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_RAISE, AudioManager
                .FLAG_SHOW_UI);
    }

    public static void subVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_LOWER, AudioManager
                .FLAG_SHOW_UI);
    }

    private MediaPlayer mMediaPlayer;

    private void releasePlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void playBeep(final Context context, final int rid) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    if (rid != 0) {
                        releasePlayer();
                        mMediaPlayer = MediaPlayer.create(context, rid);
                        mMediaPlayer.start();
                        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer players) {
                                releasePlayer();
                            }
                        });
                        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer players, int arg1, int arg2) {
                                releasePlayer();
                                return false;
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    releasePlayer();
                }
            }
        }).start();
    }
}
