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
    }

    interface OnFetchDataListener<T> {
        void onFetchDataSuccess(int position, List<T> list);

        void onFetchDataError(String message);
    }

    interface LocalDataSource {
        void getAllSongLocal(
                SongDataSource.OnGetDataLocalListener<Song> listener);

        void getSongDownload();
    }

    interface OnGetDataLocalListener<T> {
        void onGetDataLocalSuccess(List<T> list);

        void onGetDataLocalError(String message);
    }
}
