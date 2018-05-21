package com.framgia.thaihn.tmusic.data.source.remote;

import com.framgia.thaihn.tmusic.data.interator.GetDataInteractor;
import com.framgia.thaihn.tmusic.data.interator.SearchSongInteractor;
import com.framgia.thaihn.tmusic.data.model.GenreSong;
import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.data.source.SongDataSource;
import com.framgia.thaihn.tmusic.util.Utils;

public class SongRemoteDataSource implements SongDataSource.RemoteDataSource {

    private static SongRemoteDataSource sSongRemoteDataSource;

    private SongRemoteDataSource() {
    }

    public static SongRemoteDataSource getInstance() {
        if (sSongRemoteDataSource == null) {
            sSongRemoteDataSource = new SongRemoteDataSource();
        }
        return sSongRemoteDataSource;
    }

    @Override
    public void getSongsRemote(int position, int limit, int offSet,
                               SongDataSource.OnFetchDataListener<Song> listener) {
        GetDataInteractor getDataInteractor = new GetDataInteractor(listener);
        String url = Utils.createUrlContent(GenreSong.getGenres()[position], limit, offSet);
        getDataInteractor.loadData(position, url);
    }

    @Override
    public void getAllSongsRemote(int limit, int offset,
                                  SongDataSource.OnFetchDataListener<Song> listener) {
        GetDataInteractor getDataInteractor = new GetDataInteractor(listener);
        getDataInteractor.loadAllData(limit, offset);
    }

    @Override
    public void searchSong(String url, SongDataSource.OnGetDataListener<Song> listener) {
        SearchSongInteractor searchSongInteractor = new SearchSongInteractor(listener);
        searchSongInteractor.searchSong(url);
    }
}
