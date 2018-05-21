package com.framgia.thaihn.tmusic.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.util.Constants;
import com.framgia.thaihn.tmusic.util.music.MediaListener;
import com.framgia.thaihn.tmusic.util.music.MusicManager;
import com.framgia.thaihn.tmusic.util.music.StateManager;

import java.util.List;

public class MusicService extends Service implements MediaListener.ServiceListener {

    public static final String INTENT_ACTION_START_MUSIC = "START_MUSIC";
    public static final String INTENT_ACTION_PLAY_PAUSE_MUSIC = "PLAY_PAUSE_MUSIC";
    public static final String INTENT_ACTION_NEXT_MUSIC = "NEXT_MUSIC";
    public static final String INTENT_ACTION_PREVIOUS_MUSIC = "PREVIOUS_MUSIC";

    private MusicManager mMusicManager;
    private final IBinder mMusicBinder = new MusicBinder();
    private MediaListener.ServiceListener mServiceListener;

    public void setServiceListener(MediaListener.ServiceListener serviceListener) {
        mServiceListener = serviceListener;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicManager = new MusicManager(this);
        mMusicManager.setServiceListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMusicBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        receiveAction(intent);
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    public void playMusic(List<Song> songs, int position) {
        if (mMusicManager == null) {
            mMusicManager = new MusicManager(this);
        }
        mMusicManager.playMusic(songs, position);
    }

    public void playPreviousMusic() {
        if (mMusicManager == null) return;
        mMusicManager.playPreviousSong();
    }

    public void playNextMusic() {
        if (mMusicManager == null) return;
        mMusicManager.playNextSong();
    }

    public void chooseState() {
        if (mMusicManager == null) return;
        mMusicManager.chooseState();
    }

    public void seekToMusic(int progress) {
        if (mMusicManager == null) return;
        mMusicManager.seekTo(progress);
    }

    public int getCurrentPosition() {
        return mMusicManager == null ? null : mMusicManager.getCurrentPosition();
    }

    public int getCurrentDuration() {
        return mMusicManager == null ? null : mMusicManager.getCurrentDuration();
    }

    public List<Song> getSongs() {
        return mMusicManager == null ? null : mMusicManager.getSongs();
    }

    public int getState() {
        return mMusicManager == null ? StateManager.PREPARE : mMusicManager.getState();
    }

    @Override
    public void eventPause() {
        if (mServiceListener == null) return;
        mServiceListener.eventPause();
    }

    @Override
    public void eventPlay() {
        if (mServiceListener == null) return;
        mServiceListener.eventPlay();
    }

    @Override
    public void eventPlayFail(String message) {
        if (mServiceListener == null) return;
        mServiceListener.eventPlayFail(message);
    }

    @Override
    public void eventPrevious() {
        if (mServiceListener == null) return;
        mServiceListener.eventPrevious();
    }

    @Override
    public void eventPreviousFail(String message) {
        if (mServiceListener == null) return;
        mServiceListener.eventPreviousFail(message);
    }

    @Override
    public void eventNext() {
        if (mServiceListener == null) return;
        mServiceListener.eventNext();
    }

    @Override
    public void eventNextFail(String message) {
        if (mServiceListener == null) return;
        mServiceListener.eventNextFail(message);
    }

    @Override
    public void eventPlayExit() {
        if (mServiceListener == null) return;
        mServiceListener.eventPlayExit();
    }

    @Override
    public void updateSeekBar() {
        if (mServiceListener == null) return;
        mServiceListener.updateSeekBar();
    }

    @Override
    public void removeUpdateSeekBar() {
        if (mServiceListener == null) return;
        mServiceListener.removeUpdateSeekBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMusicManager.destroy();
    }

    /**
     * Check action to control music form Background Service or Foreground Service
     *
     * @param intent
     */
    private void receiveAction(Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        if (action == null) return;
        switch (action) {
            case INTENT_ACTION_START_MUSIC: {
                if (intent.getExtras() != null) {
                    int position = intent.getIntExtra(Constants.BUNDLE_POSITION_SONG, 0);
                    List<Song> list = intent.getParcelableArrayListExtra(Constants.BUNDLE_LIST_MUSIC_PLAY);
                    playMusic(list, position);
                }
                break;
            }
            case INTENT_ACTION_NEXT_MUSIC: {
                break;
            }
            case INTENT_ACTION_PREVIOUS_MUSIC: {
                break;
            }
            case INTENT_ACTION_PLAY_PAUSE_MUSIC: {
                break;
            }
        }
    }
}
