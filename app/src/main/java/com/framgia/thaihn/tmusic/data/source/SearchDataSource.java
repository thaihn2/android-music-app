package com.framgia.thaihn.tmusic.data.source;

import java.util.List;

public interface SearchDataSource {

    interface RemoteDataSource {
        void searchSong(String url, SearchDataSource.OnSearchDataListener listener);
    }

    interface OnSearchDataListener<T> {

        void onSearchDataSuccess(List<T> list);

        void onSearchDataError(String message);
    }
}
