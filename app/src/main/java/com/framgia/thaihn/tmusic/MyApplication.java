package com.framgia.thaihn.tmusic;

import android.app.Application;

import com.framgia.thaihn.tmusic.data.source.local.SongLocalDataSource;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SongLocalDataSource.getContext(this);
    }
}
