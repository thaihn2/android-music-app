package com.framgia.thaihn.tmusic.util.music;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.framgia.thaihn.tmusic.R;
import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.service.MusicService;
import com.framgia.thaihn.tmusic.util.Constants;
import com.framgia.thaihn.tmusic.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicManager implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    private MediaPlayer mMediaPlayer;
    private List<Song> mSongs;
    private int mCurrentPosition;
    private MusicService mMusicService;
    @StateManager.StateLoop
    private int mLoopType = StateManager.LOOP_DISABLE;
    @StateManager.StatePlay
    private int mState = StateManager.PREPARE;
    private MediaListener.ServiceListener mServiceListener;

    public void setServiceListener(MediaListener.ServiceListener serviceListener) {
        mServiceListener = serviceListener;
    }

    public MusicManager(MusicService musicService) {
        this.mMusicService = musicService;
    }

    /**
     * Choose state pause of play of music
     */
    public void chooseState() {
        if (mMediaPlayer == null) return;
        if (mState == StateManager.PLAYING) {
            pause();
            setState(StateManager.PAUSE);
            mServiceListener.eventPause();
        } else {
            mMediaPlayer.start();
            setState(StateManager.PLAYING);
            mServiceListener.eventPlay();
        }
    }

    /**
     * Receive control play music
     *
     * @param songs
     * @param position
     */
    public void playMusic(List<Song> songs, int position) {
        if (mSongs == null && songs == null) {
            setState(StateManager.ERROR);
            mServiceListener.eventPlayFail(
                    mMusicService.getString(R.string.error_can_not_load_list_song));
            return;
        }
        // song is playing
        if (mSongs != null && mSongs.size() != 0) {
            if (mSongs.get(mCurrentPosition).getId() == songs.get(position).getId()) {
                return;
            }
        }
        // play new song
        if (songs.size() != 0) {
            mSongs = new ArrayList<>();
            mSongs.addAll(songs);
            mCurrentPosition = position;
            prepareSong();
        }
    }

    /**
     * Play previous song if position different 0
     */
    public void playPreviousSong() {
        if (mCurrentPosition == 0) {
            mServiceListener.eventPreviousFail(mMusicService.getString(R.string.error_first_of_list_song));
            return;
        }
        mCurrentPosition--;
        mServiceListener.eventPrevious();
        prepareSong();
    }

    /**
     * Play next song if position is last and loop type is all
     */
    public void playNextSong() {
        if (mCurrentPosition == mSongs.size() - 1) {
            if (mLoopType != StateManager.LOOP_ALL) {
                mServiceListener.eventNextFail(mMusicService.getString(R.string.error_end_of_list_song));
                return;
            }
            mCurrentPosition = -1;
        }
        mCurrentPosition++;
        mServiceListener.eventNext();
        prepareSong();
    }

    /**
     * Goto part of a song
     *
     * @param progress
     */
    public void seekTo(int progress) {
        if (mMediaPlayer == null) return;
        if (mState == StateManager.PAUSE || mState == StateManager.PLAYING) {
            mMediaPlayer.seekTo(mMediaPlayer.getDuration() / Constants.DEFAULT_MAX_SEEK_BAR * progress);
        }
    }

    /**
     * Prepare song to play
     */
    private void prepareSong() {
        if (mSongs == null && mSongs.size() == 0) {
            setState(StateManager.ERROR);
            return;
        }
        reset();
        // update progress
        if (mServiceListener != null) {
            mServiceListener.removeUpdateSeekBar();
        }
        setState(StateManager.PREPARE);
        loadSong();
    }

    /**
     * Load new song
     */
    private void loadSong() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnCompletionListener(this);
        try {
            mMediaPlayer.setDataSource(mSongs.get(mCurrentPosition).getUri());
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public int getState() {
        return mState;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setLoopType(@StateManager.StateLoop int loopType) {
        mLoopType = loopType;
    }

    public int getLoopType() {
        return mLoopType;
    }

    public void setState(@StateManager.StatePlay int state) {
        mState = state;
    }

    public int getCurrentDuration() {
        if (mMediaPlayer == null) return Constants.ERROR_DURATION;
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
        setState(StateManager.PLAYING);
        mServiceListener.updateSeekBar();
        mServiceListener.eventPlay();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        switch (mLoopType) {
            case StateManager.LOOP_DISABLE: {
                // play next music
                playNextSong();
                break;
            }
            case StateManager.LOOP_ONE: {
                // play again music
                break;
            }
            case StateManager.LOOP_ALL: {
                // play next. if end of list start first list
                break;
            }
        }
    }

    /**
     * Reset media player
     */
    private void reset() {
        if (mMediaPlayer == null) return;
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    /**
     * Pause music
     */
    private void pause() {
        if (mMediaPlayer == null) return;
        mMediaPlayer.pause();
    }

    /**
     * Clear media player
     */
    public void destroy() {
        if (mMediaPlayer == null) return;
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mState = StateManager.PREPARE;
    }
}
