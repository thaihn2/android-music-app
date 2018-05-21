package com.framgia.thaihn.tmusic.data.source.remote;

import com.framgia.thaihn.tmusic.data.interator.SearchSongInteractor;
import com.framgia.thaihn.tmusic.data.source.SearchDataSource;

public class SearchRemoteDataSource implements SearchDataSource.RemoteDataSource {

    private static SearchRemoteDataSource sSearchRemoteDataSource;

    private SearchRemoteDataSource() {
    }

    public static SearchRemoteDataSource getInstance() {
        if (sSearchRemoteDataSource == null) {
            sSearchRemoteDataSource = new SearchRemoteDataSource();
        }
        return sSearchRemoteDataSource;
    }


    @Override
    public void searchSong(String url, SearchDataSource.OnSearchDataListener listener) {
        SearchSongInteractor searchSongInteractor = new SearchSongInteractor(listener);
        searchSongInteractor.searchSong(url);
    }
}
