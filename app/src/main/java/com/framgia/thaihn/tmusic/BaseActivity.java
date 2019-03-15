package com.framgia.thaihn.tmusic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.screen.detail.DetailActivity;
import com.framgia.thaihn.tmusic.service.MusicService;
import com.framgia.thaihn.tmusic.util.Constants;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getLayoutResources();

    protected abstract void initVariables(Bundle savedInstanceState);

    protected abstract void initData(Bundle savedInstanceState);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResources());
        changeColorStatusBar();
        initVariables(savedInstanceState);
        initData(savedInstanceState);
    }

    public void changeColorStatusBar() {
    }

    public void gotoPlayMusic(int position, List<Song> list) {
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra(Constants.BUNDLE_POSITION_SONG, position);
        intent.setAction(MusicService.INTENT_ACTION_START_MUSIC);
        intent.putParcelableArrayListExtra(Constants.BUNDLE_LIST_MUSIC_PLAY,
                (ArrayList<? extends Parcelable>) list);
        startService(intent);
        Intent intentDetail = new Intent(this, DetailActivity.class);
        startActivity(intentDetail);
    }
}
