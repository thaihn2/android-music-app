package com.framgia.thaihn.tmusic.data.interator;

import android.os.AsyncTask;

import com.framgia.thaihn.tmusic.data.model.GenreSong;
import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.data.model.SongEntry;
import com.framgia.thaihn.tmusic.data.source.SongDataSource;
import com.framgia.thaihn.tmusic.util.Constants;
import com.framgia.thaihn.tmusic.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
                JSONObject jsonResult = new JSONObject(getStringFromUrl(params[0]));
                return parserListSong(jsonResult);
            } catch (JSONException e) {
                e.printStackTrace();
                if (mListener != null) {
                    mListener.onFetchDataError(e.getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (mListener != null) {
                    mListener.onFetchDataError(e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Song> songs) {
            super.onPostExecute(songs);
            if (mListener != null) {
                mListener.onFetchDataSuccess(mPosition, songs);
            }
        }
    }

    /**
     * @param strUrl url of content
     * @return
     * @throws IOException
     */
    protected String getStringFromUrl(String strUrl) throws IOException {
        String result = "";
        URL url = new URL(strUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(Constants.METHOD_GET);

        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            if (in != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
            }
            in.close();
        }
        return result;
    }

    /**
     * Parser list song from content
     *
     * @param result
     * @return
     * @throws JSONException
     */
    protected List<Song> parserListSong(JSONObject result) throws JSONException {
        List<Song> listSong = new ArrayList<>();
        JSONArray jsonCollections = result.getJSONArray(SongEntry.COLLECTION);
        for (int i = 0; i < jsonCollections.length(); i++) {
            JSONObject jsonChild = jsonCollections.getJSONObject(i);
            JSONObject jsonTrack = jsonChild.getJSONObject(SongEntry.TRACK);
            Song song = parserSong(jsonTrack);
            if (song != null) {
                listSong.add(song);
            }
        }
        return listSong;
    }

    /**
     * Parser json from each Track
     *
     * @param jsonTrack
     * @return
     * @throws JSONException
     */
    protected Song parserSong(JSONObject jsonTrack) throws JSONException {
        if (jsonTrack != null) {
            Song song = new Song();
            song.setArtworkUrl(jsonTrack.optString(SongEntry.ARTWORK_URL));
            song.setCommentCount(jsonTrack.optInt(SongEntry.COMMENT_COUNT, 0));
            song.setDescription(jsonTrack.optString(SongEntry.DESCRIPTION, ""));
            song.setDownloadable(jsonTrack.optBoolean(SongEntry.DOWNLOADABLE, false));
            song.setDownloadUrl(jsonTrack.optString(SongEntry.DOWNLOAD_URL, ""));
            song.setDuration(jsonTrack.optInt(SongEntry.DURATION, 0));
            song.setGenre(jsonTrack.optString(SongEntry.GENRE, ""));
            song.setId(jsonTrack.optInt(SongEntry.ID, 0));
            song.setKind(jsonTrack.optString(SongEntry.KIND, ""));
            song.setLikesCount(jsonTrack.optInt(SongEntry.LIKES_COUNT, 0));
            song.setPermalinkUrl(jsonTrack.optString(SongEntry.PERMALINK, ""));
            song.setPlaybackCount(jsonTrack.optInt(SongEntry.PLAYBACK_COUNT, 0));
            song.setTitle(jsonTrack.optString(SongEntry.TITLE, ""));
            song.setUri(jsonTrack.optString(SongEntry.URI, ""));
            song.setUserId(jsonTrack.optInt(SongEntry.USER_ID, 0));
            JSONObject jsonUser = jsonTrack.getJSONObject(SongEntry.USER);
            song.setAvatarUrl(jsonUser.optString(SongEntry.AVATAR_URL, ""));
            song.setUsername(jsonUser.optString(SongEntry.USERNAME, ""));
            song.setPublic(jsonTrack.optBoolean(SongEntry.PUBLIC, false));
            if (song.getArtworkUrl() == null) {
                song.setArtworkUrl(song.getAvatarUrl());
            }
            return song;
        }
        return null;
    }
}
