package com.framgia.thaihn.tmusic.screen.more;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.framgia.thaihn.tmusic.BaseActivity;
import com.framgia.thaihn.tmusic.R;
import com.framgia.thaihn.tmusic.data.model.GenreSong;
import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.screen.detail.DetailActivity;
import com.framgia.thaihn.tmusic.screen.main.MainActivity;
import com.framgia.thaihn.tmusic.screen.search.SearchAdapter;
import com.framgia.thaihn.tmusic.service.MusicService;
import com.framgia.thaihn.tmusic.util.Constants;
import com.framgia.thaihn.tmusic.util.ToastUtils;
import com.framgia.thaihn.tmusic.util.music.MediaListener;
import com.framgia.thaihn.tmusic.util.music.StateManager;
import com.framgia.thaihn.tmusic.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class MoreActivity extends BaseActivity implements MoreContract.View,
        SearchAdapter.OnClickListenerSong,
        MediaListener.ServiceListener,
        View.OnClickListener {

    private int mIdGenre;
    private MoreContract.Presenter mPresenter;
    private ProgressBar mProgressBar;
    private RecyclerView mRecycle;
    private SearchAdapter mAdapter;
    private TextView mTextNotification;
    private MusicService mMusicService;
    private boolean mMusicBound = false;
    private Intent mIntent;
    private List<Song> mSongs;
    private int mPosition;
    private int mState;

    private View mViewSmallPlayer;
    private CircleImageView mImageAvatar;
    private TextView mTextName, mTextSinger;
    private ImageView mImagePrevious, mImagePlay, mImageNext;


    @Override
    protected int getLayoutResources() {
        return R.layout.activity_more;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        mProgressBar = findViewById(R.id.progress);
        mRecycle = findViewById(R.id.recycle_song);
        mTextNotification = findViewById(R.id.text_notification);
        mViewSmallPlayer = findViewById(R.id.layout_small_player);
        mTextName = findViewById(R.id.text_name);
        mTextSinger = findViewById(R.id.text_singer);
        mImagePrevious = findViewById(R.id.image_previous);
        mImagePlay = findViewById(R.id.image_play);
        mImageNext = findViewById(R.id.image_next);
        mImageAvatar = findViewById(R.id.image_avatar);

        mImageNext.setOnClickListener(this);
        mImagePlay.setOnClickListener(this);
        mImagePrevious.setOnClickListener(this);
        mViewSmallPlayer.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mIdGenre = getIntent().getIntExtra(Constants.EXTRAS_ID_GENRE, 0);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.color_persian_green)));
        getSupportActionBar().setTitle(GenreSong.getGenres()[mIdGenre].toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPresenter = new MorePresenter();
        mPresenter.setView(this);
        configRecycle(this);
        mPresenter.loadMusic(mIdGenre, 100, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showData(List<Song> list) {
        mAdapter.addData(list);
        if (mAdapter.getSongs().size() == 0) {
            showError(Constants.ERROR_NO_DATA);
        } else {
            mTextNotification.setVisibility(View.GONE);
            mRecycle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError(String message) {
        mTextNotification.setVisibility(View.VISIBLE);
        mTextNotification.setText(message);
    }

    private void configRecycle(Context context) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        mRecycle.setHasFixedSize(true);
        mAdapter = new SearchAdapter(new ArrayList<Song>());
        mAdapter.setOnClickListenerSong(this);
        mRecycle.setLayoutManager(layoutManager);
        mRecycle.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMoreClicked(int position) {
    }

    @Override
    public void onItemClicked(int position) {
        gotoPlayMusic(position, mAdapter.getSongs());
    }

    @Override
    public void changeColorStatusBar() {
        super.changeColorStatusBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.color_aqua_deep));
        }
    }

    @Override
    public void eventPrepare() {
    }

    @Override
    public void eventPause() {
        mImagePlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_media_play_symbol_white));
    }

    @Override
    public void eventPlay() {
        mViewSmallPlayer.setVisibility(View.VISIBLE);
        if (mSongs == null || mSongs.size() == 0) return;
        loadUiSmallPlayer(mSongs.get(mPosition));
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
        if (mSongs == null || mSongs.size() == 0) return;
        loadUiSmallPlayer(mSongs.get(mPosition));
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
        if (mSongs == null || mSongs.size() == 0) return;
        loadUiSmallPlayer(mSongs.get(mPosition));
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
    }

    @Override
    public void removeUpdateSeekBar() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_next: {
                if (mMusicService != null) {
                    mMusicService.playNextMusic();
                }
                break;
            }
            case R.id.layout_small_player: {
                Intent intent = new Intent(MoreActivity.this, DetailActivity.class);
                startActivity(intent);
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
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            mMusicService = binder.getService();
            mMusicService.setServiceListener(MoreActivity.this);
            mPosition = mMusicService.getCurrentPosition();
            mSongs = mMusicService.getSongs();
            mState = mMusicService.getState();
            if (mSongs != null && mSongs.size() != 0) {
                checkStatusPlayer(mState);
                loadUiSmallPlayer(mSongs.get(mPosition));
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
    protected void onStart() {
        super.onStart();
        if (mIntent == null) {
            mIntent = new Intent(this, MusicService.class);
        }
        bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMusicBound) {
            unbindService(mConnection);
            mMusicBound = false;
        }
    }

    /**
     * Load ui if music playing
     *
     * @param song
     */
    private void loadUiSmallPlayer(Song song) {
        if (song == null) return;
        mTextName.setText(song.getTitle());
        mTextSinger.setText(song.getUsername());
        Glide.with(mImageAvatar.getContext())
                .load(song.getArtworkUrl())
                .into(mImageAvatar);
        if (mState == StateManager.PLAYING) {
            mImagePlay.setImageResource(R.drawable.ic_pause_button_white);
        } else {
            mImagePlay.setImageResource(R.drawable.ic_media_play_symbol_white);
        }
    }

    private void checkStatusPlayer(int status) {
        if (status == StateManager.PLAYING || status == StateManager.PAUSE) {
            mViewSmallPlayer.setVisibility(View.VISIBLE);
        } else {
            mViewSmallPlayer.setVisibility(View.GONE);
        }
    }
}
