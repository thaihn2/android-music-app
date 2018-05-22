package com.framgia.thaihn.tmusic.data.source.local;

import android.content.Context;

import com.framgia.thaihn.tmusic.data.interator.GetSongLocalInteractor;
import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.data.source.SongDataSource;

public class SongLocalDataSource implements SongDataSource.LocalDataSource {

    private static SongLocalDataSource sSongLocalDataSource;
    private GetSongLocalInteractor mGetSongLocalInteractor;

    public static SongLocalDataSource getInstance() {
        return sSongLocalDataSource;
    }

    private SongLocalDataSource(Context context) {
        mGetSongLocalInteractor = GetSongLocalInteractor.newInstance(context);
    }

    public static void getContext(Context context) {
        if (sSongLocalDataSource == null) {
            sSongLocalDataSource = new SongLocalDataSource(context);
        }
    }

    @Override
    public void getAllSongLocal(SongDataSource.OnGetDataListener<Song> listener) {
        if (mGetSongLocalInteractor == null) return;
        mGetSongLocalInteractor.getAllSong(listener);
    }

    @Override
    public void getSongDownload(SongDataSource.OnGetDataListener<Song> listener) {
        if (mGetSongLocalInteractor != null) return;
        mGetSongLocalInteractor.getSongDownload(listener);
    }
}
