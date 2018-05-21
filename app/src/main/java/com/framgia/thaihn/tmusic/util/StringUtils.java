package com.framgia.thaihn.tmusic.util;

import com.framgia.thaihn.tmusic.BuildConfig;

public class StringUtils {

    public static String convertArtWorkUrlBetter(String artworkUrl) {
        if (artworkUrl != null) {
            return artworkUrl.replace(Constants.IMAGE_SIZE_LARGE, Constants.IMAGE_SIZE_ORIGIN);
        }
        return null;
    }

    public static String createUri(String uri) {
        return String.format("%s/%s?%s=%s", uri, Constants.STREAM, Constants.CLIENT_ID,
                BuildConfig.API_KEY);
    }

    public static String createUrlContent(String genre, int limit, int offset) {
        return String.format("%s%s%s&%s=%s&%s=%d&%s=%d", Constants.URL_BASE,
                Constants.CONTENT_URL, genre, Constants.CLIENT_ID,
                BuildConfig.API_KEY, Constants.LIMIT, limit, Constants.OFFSET, offset);
    }

    public static String createUrlSearch(String key, int limit) {
        return String.format("%s%s&%s=%d&%s=%s&%s=%s", Constants.URL_BASE_SEARCH,
                Constants.SEARCH_FILTER, Constants.LIMIT, limit,
                Constants.CLIENT_ID, BuildConfig.API_KEY, Constants.SEARCH_PARAM, key);
    }

    public static String createUrlDownload(String url) {
        return String.format("%s?%s=%s", url, Constants.CLIENT_ID, BuildConfig.API_KEY);
    }

    private StringUtils() {
    }
}
