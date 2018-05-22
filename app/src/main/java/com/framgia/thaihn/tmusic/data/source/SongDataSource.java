package com.framgia.thaihn.tmusic.data.source;

import com.framgia.thaihn.tmusic.data.model.Song;

import java.util.List;

/**
 * Save all listener of data source
 */
public interface SongDataSource {

    interface RemoteDataSource {
        void getSongsRemote(int position, int limit, int offSet,
                            OnFetchDataListener<Song> listener);

        void getAllSongsRemote(int limit, int offset,
                               OnFetchDataListener<Song> listener);

        void searchSong(String url, SongDataSource.OnGetDataListener<Song> listener);
    }

    interface OnFetchDataListener<T> {
        void onFetchDataSuccess(int position, List<T> list);

        void onFetchDataError(String message);
    }

    interface LocalDataSource {
        void getAllSongLocal(
                OnGetDataListener<Song> listener);

        void getSongDownload(OnGetDataListener<Song> listener);
    }

    interface OnGetDataListener<T> {
        void onGetDataSuccess(List<T> list);

        void onGetDataError(String message);
    }
}
