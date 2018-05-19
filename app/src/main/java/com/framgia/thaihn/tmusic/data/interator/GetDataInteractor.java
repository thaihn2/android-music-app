package com.framgia.thaihn.tmusic.data.interator;

import android.os.AsyncTask;

import com.framgia.thaihn.tmusic.data.model.GenreSong;
import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.data.source.SongDataSource;
import com.framgia.thaihn.tmusic.util.Constants;
import com.framgia.thaihn.tmusic.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class GetDataInteractor {

    protected SongDataSource.OnFetchDataListener<Song> mListener;

    public GetDataInteractor(SongDataSource.OnFetchDataListener<Song> listener) {
        this.mListener = listener;
    }

    public void loadData(int position, String url) {
        new FetchData(position).execute(url);
    }

    public void loadAllData(int limit, int offset) {
        for (int i = 0; i < GenreSong.getGenres().length; i++) {
            new FetchData(i).execute(Utils.createUrlContent(
                    GenreSong.getGenres()[i],
                    limit, offset
            ));
        }
    }

    /**
     * Fetch data using async task by genre
     */
    public class FetchData extends AsyncTask<String, Void, List<Song>> {

        private int mPosition;

        public FetchData(int position) {
            this.mPosition = position;
        }

        @Override
        protected List<Song> doInBackground(String... params) {
            try {
                ParserInteractor parserInteractor = new ParserInteractor();
                String result = parserInteractor.getContentFromUrl(params[0]);
                if (result != null) {
                    JSONObject jsonResult = new JSONObject(result);
                    return parserInteractor.parserListSong(jsonResult);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Song> songs) {
            super.onPostExecute(songs);
            if (mListener == null) return;
            if (songs != null) {
                mListener.onFetchDataSuccess(mPosition, songs);
            } else {
                mListener.onFetchDataError(Constants.ERROR_NO_DATA);
            }
        }
    }
}
