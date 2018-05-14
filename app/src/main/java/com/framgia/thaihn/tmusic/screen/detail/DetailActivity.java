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
import com.framgia.thaihn.tmusic.service.MusicService;
import com.framgia.thaihn.tmusic.util.Constants;
import com.framgia.thaihn.tmusic.util.ToastUtils;
import com.framgia.thaihn.tmusic.util.Utils;
import com.framgia.thaihn.tmusic.util.music.MediaListener;

import java.util.List;

public class DetailActivity extends BaseActivity
        implements View.OnClickListener,
        MediaListener.ServiceListener, SeekBar.OnSeekBarChangeListener {

    private List<Song> mSongs;
    private int mPosition;
    private MusicService mMusicService;
    private boolean mMusicBound;
    private boolean mSeekByUser;
    private Intent mIntent;
    private int mCurrentProgress;


    private ImageView mImageAvatar, mImageLike, mImageDownload, mImageShare,
            mImageRandom, mImagePrevious, mImagePlay, mImageNext, mImageLoop;
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
        mImageRandom = findViewById(R.id.image_random);
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
        mImageRandom.setOnClickListener(this);
        mImagePrevious.setOnClickListener(this);
        mImageNext.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        changeColorStatusBar();
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
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mIntent == null) {
            mIntent = new Intent(this, MusicService.class);
        }
        bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE);
        startService(mIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMusicBound) {
            unbindService(mConnection);
            mMusicBound = false;
        }
    }

    @Override
    public void eventPause() {
        mImagePlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_media_play_symbol_white));
    }

    @Override
    public void eventPlay() {
        mImagePlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_button_white));
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
        loadUi(mSongs.get(mPosition));
    }

    @Override
    public void eventNextFail(String message) {
        ToastUtils.quickToast(this, message, Toast.LENGTH_SHORT);
    }

    @Override
    public void eventPlayExit() {
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
            loadUi(mSongs.get(mPosition));
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

    private void checkDownload() {
        if (mMusicService != null) {
            if (mSongs.get(mPosition).isDownloadable()) {
                // TODO download music in here
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
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_warning)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        Glide.with(mImageAvatar.getContext())
                .applyDefaultRequestOptions(options)
                .load(Utils.convertArtWorkUrlBetter(song.getArtworkUrl()))
                .into(mImageAvatar);
        if (!song.isDownloadable()) {
            mImageDownload.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_download_grey));
        } else {
            mImageDownload.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_download_white));
        }
        mImagePlay.setImageDrawable(
                getResources().getDrawable(R.drawable.ic_media_play_symbol_white));
        mSeekbarPlay.setMax(Constants.DEFAULT_MAX_SEEK_BAR);
        mSeekbarPlay.setProgress(0);
        mSeekbarPlay.setOnSeekBarChangeListener(this);
    }

    /**
     * Change color of status bar
     */
    private void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.color_chocolate));
        }
    }
}
