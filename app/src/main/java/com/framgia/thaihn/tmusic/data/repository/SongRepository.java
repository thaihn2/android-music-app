package com.framgia.thaihn.tmusic.data.repository;

import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.data.source.SongDataSource;
import com.framgia.thaihn.tmusic.data.source.remote.SongRemoteDataSource;

public class SongRepository implements SongDataSource.RemoteDataSource {

    private static SongRepository sSongRepository;
    private SongDataSource.RemoteDataSource mRemoteDataSource;

    private SongRepository(SongDataSource.RemoteDataSource remote) {
        mRemoteDataSource = remote;
    }

    public static SongRepository getInstace() {
        if (sSongRepository == null) {
            sSongRepository = new SongRepository(SongRemoteDataSource.getInstance());
        }
        return sSongRepository;
    }

    @Override
    public void getSongsRemote(int position, int limit, int offSet,
                               SongDataSource.OnFetchDataListener<Song> listener) {
        if (mRemoteDataSource == null) {
            return;
        }
        mRemoteDataSource.getSongsRemote(position, limit, offSet, listener);
    }

    @Override
    public void getAllSongsRemote(int limit, int offset,
                                  SongDataSource.OnFetchDataListener<Song> listener) {
        if (mRemoteDataSource == null) {
            return;
        }
        mRemoteDataSource.getAllSongsRemote(limit, offset, listener);
    }
}
