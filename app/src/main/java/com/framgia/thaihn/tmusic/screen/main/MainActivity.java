package com.framgia.thaihn.tmusic.screen.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.framgia.thaihn.tmusic.BaseActivity;
import com.framgia.thaihn.tmusic.R;
import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.screen.detail.DetailActivity;
import com.framgia.thaihn.tmusic.screen.genre.GenreFragment;
import com.framgia.thaihn.tmusic.screen.personal.PersonalFragment;
import com.framgia.thaihn.tmusic.screen.search.SearchActivity;
import com.framgia.thaihn.tmusic.service.MusicService;
import com.framgia.thaihn.tmusic.util.FragmentUtils;
import com.framgia.thaihn.tmusic.util.ToastUtils;
import com.framgia.thaihn.tmusic.util.music.MediaListener;
import com.framgia.thaihn.tmusic.util.music.StateManager;
import com.framgia.thaihn.tmusic.widget.CircleImageView;

import java.util.List;

public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, MediaListener.ServiceListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private BottomNavigationView mBottomTabMain;
    private View mViewSmallPlayer;
    private CircleImageView mImageAvatar;
    private TextView mTextName, mTextSinger;
    private ImageView mImagePrevious, mImagePlay, mImageNext;

    private MusicService mMusicService;
    private boolean mMusicBound = false;
    private Intent mIntent;
    private GenreFragment mGenreFragment;
    private PersonalFragment mPersonalFragment;
    private List<Song> mSongs;
    private int mPosition;
    private int mState;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        mNavigationView = findViewById(R.id.nav_view);
        mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mBottomTabMain = findViewById(R.id.bottom_navigation);
        mViewSmallPlayer = findViewById(R.id.layout_small_player);
        mTextName = findViewById(R.id.text_name);
        mTextSinger = findViewById(R.id.text_singer);
        mImagePrevious = findViewById(R.id.image_previous);
        mImagePlay = findViewById(R.id.image_play);
        mImageNext = findViewById(R.id.image_next);
        mImageAvatar = findViewById(R.id.image_avatar);

        mNavigationView.setNavigationItemSelectedListener(this);
        mBottomTabMain.setOnNavigationItemSelectedListener(this);
        mImageNext.setOnClickListener(this);
        mImagePlay.setOnClickListener(this);
        mImagePrevious.setOnClickListener(this);
        mViewSmallPlayer.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.color_persian_green)));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // init fragment
        mGenreFragment = GenreFragment.newInstance();
        mPersonalFragment = PersonalFragment.newInstance();
        mBottomTabMain.setSelectedItemId(R.id.menu_home);
        switchTab(mBottomTabMain.getSelectedItemId());
    }


    @Override
    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.color_aqua_deep));
        }
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
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
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

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            mMusicService = binder.getService();
            mMusicService.setServiceListener(MainActivity.this);
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
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search: {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switchTab(item.getItemId());
        switch (item.getItemId()) {
            case R.id.menu_rate: {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details")));
                break;
            }
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return true;
    }

    /**
     * Switch tab
     *
     * @param id
     */
    private void switchTab(int id) {
        switch (id) {
            case R.id.menu_home: {
                FragmentUtils.replaceFragmentNotStack(
                        this,
                        mGenreFragment,
                        R.id.frame_main);
                break;
            }
            case R.id.menu_personal: {
                FragmentUtils.replaceFragmentNotStack(
                        this,
                        mPersonalFragment,
                        R.id.frame_main);
                break;
            }
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
