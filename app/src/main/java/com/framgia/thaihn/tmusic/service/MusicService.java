package com.framgia.thaihn.tmusic.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.framgia.thaihn.tmusic.R;
import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.screen.detail.DetailActivity;
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
    public static final String INTENT_ACTION_OPEN_APP = "OPEN_APP";
    public static final String CHANNEL_ID = "Music";
    public static final String CHANNEL_NAME = "TMUsic";

    private static final int ORDER_ACTION_PREVIOUS = 0;
    private static final int ORDER_ACTION_PLAY_PAUSE = 1;
    private static final int ORDER_ACTION_NEXT = 2;
    public static final int DEFAULT_ID_NOTIFICATION = 999;

    private MusicManager mMusicManager;
    private final IBinder mMusicBinder = new MusicBinder();
    private MediaListener.ServiceListener mServiceListener;
    private PendingIntent mPendingNext;
    private PendingIntent mPendingPrevious;
    private PendingIntent mPendingPlay;
    private PendingIntent mPendingItem;
    private NotificationCompat.Builder mBuilder;
    private NotificationManagerCompat mNotificationManager;
    private PhoneStateListener mPhoneStateListener;
    private TelephonyManager mTelephonyManager;

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
        phoneListener();
        mNotificationManager = NotificationManagerCompat.from(this);
        createNotificationChannel();
        createNotification();
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

    public String getTitleSong() {
        return mMusicManager == null ? null : mMusicManager.getTitle();
    }

    public String getSingerSong() {
        return mMusicManager == null ? null : mMusicManager.getNameSinger();
    }

    public void pauseMusic() {
        if (mMusicManager == null) return;
        mMusicManager.pause();
    }

    public void startMusic() {
        if (mMusicManager == null) return;
        mMusicManager.start();
    }

    @Override
    public void eventPrepare() {
        if (mServiceListener == null) return;
        mServiceListener.eventPrepare();
        updateNotification();
    }

    @Override
    public void eventPause() {
        if (mServiceListener == null) return;
        mServiceListener.eventPause();
        updateNotification();
    }

    @Override
    public void eventPlay() {
        if (mServiceListener == null) return;
        mServiceListener.eventPlay();
        updateNotification();
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
        updateNotification();
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
        updateNotification();
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
        if (mTelephonyManager != null) {
            mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        if (mMusicManager == null) return;
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
                playNextMusic();
                updateNotification();
                break;
            }
            case INTENT_ACTION_PREVIOUS_MUSIC: {
                playPreviousMusic();
                updateNotification();
                break;
            }
            case INTENT_ACTION_PLAY_PAUSE_MUSIC: {
                chooseState();
                updateNotification();
                break;
            }
        }
    }

    private void createNotificationChannel() {
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            manager.createNotificationChannel(mChannel);
        }
    }

    private void createNotification() {
        Intent intentItem = new Intent(this, DetailActivity.class);
        intentItem.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentItem.setAction(INTENT_ACTION_OPEN_APP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intentItem);
        mPendingItem = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentNext = new Intent(this, MusicService.class);
        intentNext.setAction(INTENT_ACTION_NEXT_MUSIC);
        mPendingNext = PendingIntent.getService(this,
                0, intentNext, 0);

        Intent intentPrevious = new Intent(this, MusicService.class);
        intentPrevious.setAction(INTENT_ACTION_PREVIOUS_MUSIC);
        mPendingPrevious = PendingIntent.getService(this,
                0, intentPrevious, 0);

        Intent intentPlay = new Intent(this, MusicService.class);
        intentPlay.setAction(INTENT_ACTION_PLAY_PAUSE_MUSIC);
        mPendingPlay = PendingIntent.getService(this,
                0, intentPlay, 0);
        updateNotification();
    }

    private void updateNotification() {
        if (mMusicManager == null) return;
        if (mMusicManager.getState() == StateManager.PLAYING) {
            startForeground(DEFAULT_ID_NOTIFICATION, buildNotification());
        } else {
            mNotificationManager.notify(DEFAULT_ID_NOTIFICATION, buildNotification());
            stopForeground(false);
        }
    }

    /**
     * Build notification with param and action
     *
     * @return
     */
    private Notification buildNotification() {
        int iconPlayPause = mMusicManager.getState() == StateManager.PLAYING ?
                R.drawable.ic_pause_button_white : R.drawable.ic_media_play_symbol_white;
        mBuilder = new NotificationCompat.Builder(this)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle(getTitleSong())
                .setContentText(getSingerSong())
                .setSmallIcon(R.drawable.ic_music_player)
                .setContentIntent(mPendingItem)
                .addAction(R.drawable.ic_previous_white, "", mPendingPrevious)
                .addAction(iconPlayPause, "", mPendingPlay)
                .addAction(R.drawable.ic_next_white, "", mPendingNext)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(ORDER_ACTION_PREVIOUS,
                                ORDER_ACTION_PLAY_PAUSE, ORDER_ACTION_NEXT));
        Notification notification = mBuilder.build();
        return notification;
    }

    public void phoneListener() {
        mPhoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    //Incoming call: Pause music
                    pauseMusic();
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                    //Not in call: Play music
                    startMusic();
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    //A call is dialing, active or on hold
                    pauseMusic();
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (mTelephonyManager != null) {
            mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
}
