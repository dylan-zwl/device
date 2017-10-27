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

    public int getMaxVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    public int getVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public void openVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager
                .FLAG_SHOW_UI);
    }

    public void setVolume(Context context, int volume) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_VIBRATE);
    }


    public void addVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager
                .FLAG_SHOW_UI);
    }

    public void subVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager
                .FLAG_SHOW_UI);
    }

    private void releasePlayer(MediaPlayer players) {
        if (players != null) {
            players.release();
        }
    }

    public void playBeep(final Context context, final int rid) {
        new Thread(new Runnable() {
            public void run() {
                MediaPlayer mediaPlayer = null;
                try {
                    if (rid != 0) {
                        mediaPlayer = MediaPlayer.create(context.getApplicationContext(), rid);
                        if (mediaPlayer != null) {
                            mediaPlayer.start();
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer players) {
                                    releasePlayer(players);
                                }
                            });
                            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                @Override
                                public boolean onError(MediaPlayer players, int arg1, int arg2) {
                                    releasePlayer(players);
                                    return false;
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    releasePlayer(mediaPlayer);
                }
            }
        }).start();
    }
}
