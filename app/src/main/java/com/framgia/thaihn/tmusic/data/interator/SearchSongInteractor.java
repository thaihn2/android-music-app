package com.framgia.thaihn.tmusic.data.interator;

import android.os.AsyncTask;

import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.data.source.SearchDataSource;
import com.framgia.thaihn.tmusic.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchSongInteractor {

    protected SearchDataSource.OnSearchDataListener<Song> mListener;

    public SearchSongInteractor(SearchDataSource.OnSearchDataListener<Song> listener) {
        this.mListener = listener;
    }

    public void searchSong(String url) {
        new SearchData().execute(url);
    }

    public class SearchData extends AsyncTask<String, Void, List<Song>> {

        @Override
        protected List<Song> doInBackground(String... params) {
            try {
                ParserInteractor parserInteractor = new ParserInteractor();
                String result = parserInteractor.getContentFromUrl(params[0]);
                if (result != null) {
                    return parserListSong(result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Song> songs) {
            super.onPostExecute(songs);
            if (mListener == null) return;
            if (songs != null) {
                mListener.onSearchDataSuccess(songs);
            } else {
                mListener.onSearchDataError(Constants.ERROR_NO_DATA);
            }
        }
    }

    private List<Song> parserListSong(String data) throws JSONException {
        List<Song> list = new ArrayList<>();
        JSONArray jsonResult = new JSONArray(data);
        ParserInteractor parserInteractor = new ParserInteractor();
        for (int i = 0; i < jsonResult.length(); i++) {
            JSONObject jsonSong = jsonResult.getJSONObject(i);
            Song song = parserInteractor.parserSong(jsonSong);
            if (song != null) {
                list.add(song);
            }
        }
        return list;
    }
}
