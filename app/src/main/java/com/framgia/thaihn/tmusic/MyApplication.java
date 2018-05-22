package com.framgia.thaihn.tmusic;

import android.app.Application;

import com.framgia.thaihn.tmusic.data.source.local.SongLocalDataSource;
import com.framgia.thaihn.tmusic.util.PreferencesUtils;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SongLocalDataSource.getContext(this);
        PreferencesUtils.init(this);
    }
}
