package com.framgia.thaihn.tmusic.data.interator;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.data.source.SongDataSource;

import java.util.ArrayList;
import java.util.List;

public class GetSongLocalInteractor {

    public static final String IS_MUSIC_CONDITION = " = 1";

    private Context mContext;
    private static GetSongLocalInteractor mGetSongLocalInteractor;

    public GetSongLocalInteractor(Context context) {
        this.mContext = context;
    }

    public static GetSongLocalInteractor newInstance(Context context) {
        if (mGetSongLocalInteractor == null) {
            mGetSongLocalInteractor = new GetSongLocalInteractor(context);
        }
        return mGetSongLocalInteractor;
    }

    /**
     * Get all song form sd-card using content provider
     *
     * @return
     */
    public void getAllSong(SongDataSource.OnGetDataListener<Song> listener) {
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projections = new String[]{
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns._ID};
        Cursor cursor = mContext.getContentResolver().query(
                uri,
                projections,
                MediaStore.Audio.Media.IS_MUSIC + IS_MUSIC_CONDITION,
                null, null);
        getSongFromCursor(cursor, listener);
    }

    /**
     * Get all song from cursor
     *
     * @param cursor
     * @return
     */
    private void getSongFromCursor(Cursor cursor,
                                   SongDataSource.OnGetDataListener<Song> listener) {
        if (cursor == null) {
            listener.onGetDataError(null);
            return;
        }
        List<Song> list = new ArrayList<>();
        int indexTitle = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
        int indexData = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
        int indexUserName = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
        int indexDuration = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION);
        int indexID = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
        while (cursor.moveToNext()) {
            Song song = new Song();
            song.setTitle(cursor.getString(indexTitle));
            song.setUri(cursor.getString(indexData));
            song.setUsername(cursor.getString(indexUserName));
            song.setDuration(cursor.getInt(indexDuration));
            song.setId(indexID);
            list.add(song);
        }
        cursor.close();
        listener.onGetDataSuccess(list);
    }
}
