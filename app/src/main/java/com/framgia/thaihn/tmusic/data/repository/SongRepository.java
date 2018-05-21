package com.framgia.thaihn.tmusic.data.repository;

import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.data.source.SongDataSource;
import com.framgia.thaihn.tmusic.data.source.local.SongLocalDataSource;
import com.framgia.thaihn.tmusic.data.source.remote.SongRemoteDataSource;

public class SongRepository implements SongDataSource.RemoteDataSource,
        SongDataSource.LocalDataSource {

    private static SongRepository sSongRepository;
    private SongDataSource.RemoteDataSource mRemoteDataSource;
    private SongDataSource.LocalDataSource mLocalDataSource;

    private SongRepository(SongDataSource.RemoteDataSource remote,
                           SongDataSource.LocalDataSource local) {
        mRemoteDataSource = remote;
        mLocalDataSource = local;
    }

    public static SongRepository getInstance() {
        if (sSongRepository == null) {
            sSongRepository = new SongRepository(SongRemoteDataSource.getInstance(),
                    SongLocalDataSource.getInstance());
        }
        return sSongRepository;
    }

    @Override
    public void getSongsRemote(int position, int limit, int offSet,
                               SongDataSource.OnFetchDataListener<Song> listener) {
        if (mRemoteDataSource == null) return;
        mRemoteDataSource.getSongsRemote(position, limit, offSet, listener);
    }

    @Override
    public void getAllSongsRemote(int limit, int offset,
                                  SongDataSource.OnFetchDataListener<Song> listener) {
        if (mRemoteDataSource == null) return;
        mRemoteDataSource.getAllSongsRemote(limit, offset, listener);
    }


    @Override
    public void getAllSongLocal(SongDataSource.OnGetDataListener<Song> listener) {
        if (mLocalDataSource == null) return;
        mLocalDataSource.getAllSongLocal(listener);
    }

    @Override
    public void getSongDownload() {

    }

    @Override
    public void searchSong(String url, SongDataSource.OnGetDataListener listener) {
        if (mRemoteDataSource == null) return;
        mRemoteDataSource.searchSong(url, listener);
    }
}
