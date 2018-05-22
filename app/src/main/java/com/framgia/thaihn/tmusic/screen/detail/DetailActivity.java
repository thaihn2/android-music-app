package com.framgia.thaihn.tmusic.screen.detail;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.framgia.thaihn.tmusic.BaseActivity;
import com.framgia.thaihn.tmusic.R;
import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.service.DownloadMusicService;
import com.framgia.thaihn.tmusic.service.MusicService;
import com.framgia.thaihn.tmusic.util.Constants;
import com.framgia.thaihn.tmusic.util.PreferencesUtils;
import com.framgia.thaihn.tmusic.util.StringUtils;
import com.framgia.thaihn.tmusic.util.ToastUtils;
import com.framgia.thaihn.tmusic.util.Utils;
import com.framgia.thaihn.tmusic.util.music.MediaListener;
import com.framgia.thaihn.tmusic.util.music.StateManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DetailActivity extends BaseActivity
        implements View.OnClickListener,
        MediaListener.ServiceListener, SeekBar.OnSeekBarChangeListener {

    public static final int INIT_DELAY_EXECUTOR = 0;
    public static final int PERIOD_EXECUTOR = 1;

    private List<Song> mSongs;
    private int mPosition;
    private int mState;
    private MusicService mMusicService;
    private boolean mMusicBound;
    private boolean mSeekByUser;
    private Intent mIntent;
    private int mCurrentProgress;
    private ExecutorService mExecutorService;
    private ScheduledExecutorService mScheduledExecutorService;

    private ImageView mImageAvatar, mImageLike, mImageDownload, mImageShare,
            mImageSuffer, mImagePrevious, mImagePlay, mImageNext, mImageLoop;
    private TextView mTextNameSong, mTextNameSinger, mTextTimeProgress, mTextDuration;
    private SeekBar mSeekbarPlay;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_details;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        mImageAvatar = findViewById(R.id.image_avatar);
        mImageLike = findViewById(R.id.image_like);
        mImageDownload = findViewById(R.id.image_download);
        mImageShare = findViewById(R.id.image_share);
        mImageSuffer = findViewById(R.id.image_suffer);
        mImagePrevious = findViewById(R.id.image_previous);
        mImagePlay = findViewById(R.id.image_play);
        mImageNext = findViewById(R.id.image_next);
        mImageLoop = findViewById(R.id.image_loop);
        mTextNameSong = findViewById(R.id.text_name_song);
        mTextNameSinger = findViewById(R.id.text_singer_song);
        mTextTimeProgress = findViewById(R.id.text_time_progress);
        mTextDuration = findViewById(R.id.text_duration);
        mSeekbarPlay = findViewById(R.id.seek_bar);

        findViewById(R.id.image_back).setOnClickListener(this);
        mImageDownload.setOnClickListener(this);
        mImageLoop.setOnClickListener(this);
        mImageLike.setOnClickListener(this);
        mImagePlay.setOnClickListener(this);
        mImageShare.setOnClickListener(this);
        mImageSuffer.setOnClickListener(this);
        mImagePrevious.setOnClickListener(this);
        mImageNext.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.color_chocolate));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back: {
                onBackPressed();
                break;
            }
            case R.id.image_download: {
                checkDownload();
                break;
            }
            case R.id.image_play: {
                if (mMusicService != null) {
                    mMusicService.chooseState();
                }
                break;
            }
            case R.id.image_previous: {
                if (mMusicService != null) {
                    mMusicService.playPreviousMusic();
                }
                break;
            }
            case R.id.image_next: {
                if (mMusicService != null) {
                    mMusicService.playNextMusic();
                }
                break;
            }
            case R.id.image_suffer: {
                updateSuffer();
                break;
            }
            case R.id.image_loop: {
                updateLoop();
                break;
            }
            case R.id.image_share: {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mSongs.get(mPosition).getUri());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            }
        }
    }

    private void updateLoop() {
        if (PreferencesUtils.getPlayMode() == StateManager.LOOP_ALL) {
            PreferencesUtils.savePlayMode(StateManager.LOOP_ONE);
            mImageLoop.setImageResource(R.drawable.ic_repeat_one_white_24dp);
        } else if (PreferencesUtils.getPlayMode() == StateManager.LOOP_ONE) {
            PreferencesUtils.savePlayMode(StateManager.LOOP_DISABLE);
            mImageLoop.setImageResource(R.drawable.ic_repeat_grey_24dp);
        } else if (PreferencesUtils.getPlayMode() == StateManager.LOOP_DISABLE) {
            PreferencesUtils.savePlayMode(StateManager.LOOP_ALL);
            mImageLoop.setImageResource(R.drawable.ic_repeat_white_24dp);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mIntent == null) {
            mIntent = new Intent(this, MusicService.class);
        }
        bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE);
        if (mState == StateManager.PLAYING) {
            startProgressUpdate();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMusicBound) {
            unbindService(mConnection);
            mMusicBound = false;
        }
        removeProgressUpdate();
    }

    @Override
    public void eventPrepare() {
        if (mMusicService != null) {
            mPosition = mMusicService.getCurrentPosition();
            mSongs = mMusicService.getSongs();
        }
        if (mSongs == null) return;
        loadUi(mSongs.get(mPosition));
        removeUpdateSeekBar();
    }

    @Override
    public void eventPause() {
        mImagePlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_media_play_symbol_white));
        removeProgressUpdate();
    }

    @Override
    public void eventPlay() {
        mImagePlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_button_white));
        startProgressUpdate();
    }

    @Override
    public void eventPlayFail(String message) {
        ToastUtils.quickToast(this, message, Toast.LENGTH_SHORT);
    }

    @Override
    public void eventPrevious() {
        if (mMusicService != null) {
            mPosition = mMusicService.getCurrentPosition();
            mSongs = mMusicService.getSongs();
        }
        if (mSongs == null) return;
        loadUi(mSongs.get(mPosition));
    }

    @Override
    public void eventPreviousFail(String message) {
        ToastUtils.quickToast(this, message, Toast.LENGTH_SHORT);
    }

    @Override
    public void eventNext() {
        if (mMusicService != null) {
            mPosition = mMusicService.getCurrentPosition();
            mSongs = mMusicService.getSongs();
        }
        if (mSongs == null) return;
        loadUi(mSongs.get(mPosition));
    }

    @Override
    public void eventNextFail(String message) {
        ToastUtils.quickToast(this, message, Toast.LENGTH_SHORT);
    }

    @Override
    public void eventPlayExit() {
    }

    @Override
    public void updateSeekBar() {
        startProgressUpdate();
    }

    @Override
    public void removeUpdateSeekBar() {
        removeProgressUpdate();
        mSeekbarPlay.setProgress(0);
    }


    /**
     * Create bound service to connect and control service
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            mMusicService = binder.getService();
            mMusicService.setServiceListener(DetailActivity.this);
            mPosition = mMusicService.getCurrentPosition();
            mSongs = mMusicService.getSongs();
            mState = mMusicService.getState();
            if (mSongs != null && mSongs.size() != 0) {
                loadUi(mSongs.get(mPosition));
            }
            mMusicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicService = null;
            mMusicBound = false;
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mCurrentProgress = progress;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mSeekByUser = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mSeekByUser = false;
        if (mMusicService != null) {
            mMusicService.seekToMusic(mCurrentProgress);
        }
    }

    /**
     * Check download
     */
    private void checkDownload() {
        if (mMusicService != null) {
            if (mSongs.get(mPosition).isDownloadable()) {
                Intent intent = new Intent(this, DownloadMusicService.class);
                intent.putExtra(Constants.INTENT_URL_DOWNLOAD, mSongs.get(mPosition).getUri());
                intent.putExtra(Constants.INTENT_TITLE_DOWNLOAD, mSongs.get(mPosition).getTitle());
                startService(intent);
            } else {
                ToastUtils.quickToast(getApplicationContext(),
                        getString(R.string.str_can_not_download),
                        Toast.LENGTH_SHORT);
            }
        }
    }

    /**
     * Load all ui with song
     *
     * @param song
     */
    private void loadUi(Song song) {
        if (song == null) return;
        mTextNameSong.setText(song.getTitle());
        mTextNameSinger.setText(song.getUsername());
        mTextDuration.setText(Utils.calculatorDuration(song.getDuration()));
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_music_player_large)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        Glide.with(mImageAvatar.getContext())
                .applyDefaultRequestOptions(options)
                .load(StringUtils.convertArtWorkUrlBetter(song.getArtworkUrl()))
                .into(mImageAvatar);
        if (!song.isDownloadable()) {
            mImageDownload.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_download_grey));
        } else {
            mImageDownload.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_download_white));
        }
        if (mState == StateManager.PLAYING) {
            mImagePlay.setImageResource(R.drawable.ic_pause_button_white);
        } else {
            mImagePlay.setImageResource(R.drawable.ic_media_play_symbol_white);
        }
        mSeekbarPlay.setMax(Constants.DEFAULT_MAX_SEEK_BAR);
        mSeekbarPlay.setProgress(0);
        mSeekbarPlay.setOnSeekBarChangeListener(this);

        if (mState == StateManager.PLAYING) {
            updateSeekBar();
        } else if (mState == StateManager.PAUSE) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    calculatorProgress();
                }
            });
        } else {
            mTextTimeProgress.setText(getString(R.string.time_default));
        }
        updateSuffer();
    }

    private void updateSuffer() {
        if (PreferencesUtils.getSuffer() == StateManager.SUFFER_OFF) {
            mImageSuffer.setImageResource(R.drawable.ic_shuffle_grey);
            PreferencesUtils.saveSuffer(StateManager.SUFFER_ON);
        } else {
            mImageSuffer.setImageResource(R.drawable.ic_shuffle_white);
            PreferencesUtils.saveSuffer(StateManager.SUFFER_OFF);
        }
    }

    /**
     * User ScheduleExecutorService to create schedule call again in one second
     * Better than user Thread
     */
    private void startProgressUpdate() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newCachedThreadPool();
        }
        if (mScheduledExecutorService == null) {
            mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }
        mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                mExecutorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        calculatorProgress();
                    }
                });
            }
        }, INIT_DELAY_EXECUTOR, PERIOD_EXECUTOR, TimeUnit.SECONDS);
    }

    private void calculatorProgress() {
        if (mMusicService == null) return;
        if (mSeekByUser) return;
        int currentDuration = mMusicService.getCurrentDuration();
        double progress = (double) currentDuration / mSongs.get(mPosition).getDuration()
                * Constants.DEFAULT_MAX_SEEK_BAR;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Su dung hoat canh dong giua hien tai va muc tieu
            mSeekbarPlay.setProgress((int) progress, true);
        } else {
            mSeekbarPlay.setProgress((int) progress);
        }
        long time = (long) (mSongs.get(mPosition).getDuration() /
                Constants.DEFAULT_MAX_SEEK_BAR * progress);
        mTextTimeProgress.setText(Utils.calculatorDuration(time));
    }

    private void removeProgressUpdate() {
        if (mExecutorService == null) return;

        mExecutorService.shutdownNow();
        mExecutorService = null;
        mScheduledExecutorService = null;
    }
}
