package com.framgia.thaihn.tmusic.data.interator;

import com.framgia.thaihn.tmusic.data.model.Song;
import com.framgia.thaihn.tmusic.data.model.SongEntry;
import com.framgia.thaihn.tmusic.util.Constants;
import com.framgia.thaihn.tmusic.util.StringUtils;
import com.framgia.thaihn.tmusic.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ParserInteractor {

    /**
     * Get content string form URL
     *
     * @param strUrl
     * @return
     * @throws IOException
     */
    public String getContentFromUrl(String strUrl) throws IOException {
        String content = "";
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        url = new URL(strUrl);
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(Constants.METHOD_GET);
        httpURLConnection.setConnectTimeout(Constants.CONNECTION_TIME_OUT);
        httpURLConnection.setReadTimeout(Constants.READ_INPUT_TIME_OUT);
        httpURLConnection.setDoInput(true);
        httpURLConnection.connect();
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            content = parserResultFromContent(httpURLConnection.getInputStream());
        }
        return content;
    }

    /**
     * Parser result from input stream
     *
     * @param is
     * @return
     * @throws IOException
     */
    public String parserResultFromContent(InputStream is) throws IOException {
        String result = "";
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Constants.CHARSET_NAME_UTF8));
        String line = "";
        while ((line = reader.readLine()) != null) {
            result += line;
        }
        is.close();
        return result;
    }

    /**
     * Parser list song from content
     *
     * @param result
     * @return
     * @throws JSONException
     */
    public List<Song> parserListSong(JSONObject result) throws JSONException {
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
    public Song parserSong(JSONObject jsonTrack) throws JSONException {
        if (jsonTrack == null) return null;
        Song song = new Song();
        song.setArtworkUrl(jsonTrack.optString(SongEntry.ARTWORK_URL, null));
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
        song.setUri(StringUtils.createUri(jsonTrack.optString(SongEntry.URI, "")));
        song.setUserId(jsonTrack.optInt(SongEntry.USER_ID, 0));
        JSONObject jsonUser = jsonTrack.getJSONObject(SongEntry.USER);
        song.setAvatarUrl(jsonUser.optString(SongEntry.AVATAR_URL, null));
        song.setUsername(jsonUser.optString(SongEntry.USERNAME, ""));
        song.setPublic(jsonTrack.optBoolean(SongEntry.PUBLIC, false));
        if (song.getArtworkUrl() == null) {
            song.setArtworkUrl(song.getAvatarUrl());
        }
        if (song.getArtworkUrl() == null) return null;
        return song;
    }
}
