package com.framgia.thaihn.tmusic.data.repository;

import com.framgia.thaihn.tmusic.data.source.SearchDataSource;
import com.framgia.thaihn.tmusic.data.source.remote.SearchRemoteDataSource;

public class SearchRepository implements SearchDataSource.RemoteDataSource {

    private static SearchRepository sSearchRepository;
    private SearchDataSource.RemoteDataSource mRemoteDataSource;

    private SearchRepository(SearchDataSource.RemoteDataSource remoteDataSource) {
        this.mRemoteDataSource = remoteDataSource;
    }

    public static SearchRepository getInstance() {
        if (sSearchRepository == null) {
            sSearchRepository = new SearchRepository(SearchRemoteDataSource.getInstance());
        }
        return sSearchRepository;
    }

    @Override
    public void searchSong(String url, SearchDataSource.OnSearchDataListener listener) {
        if (mRemoteDataSource == null) return;
        mRemoteDataSource.searchSong(url, listener);
    }
}
