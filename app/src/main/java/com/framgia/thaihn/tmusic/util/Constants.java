package com.framgia.thaihn.tmusic.util;

public final class Constants {

    public static final String URL_BASE = "https://api-v2.soundcloud.com/";
    public static final String URL_BASE_SEARCH = "http://api.soundcloud.com/tracks";
    public static final String CONTENT_URL = "charts?kind=top&genre=soundcloud%3Agenres%3A";
    public static final String LIMIT = "limit";
    public static final String OFFSET = "offset";
    public static final String CLIENT_ID = "client_id";
    public static final String SEARCH_FILTER = "?filter=public";
    public static final String SEARCH_PARAM = "q";

    public static final String STREAM = "stream";
    public static final String METHOD_GET = "GET";
    public static final String ERROR_NO_DATA = "Không có dữ liệu";
    public static final String CHARSET_NAME_UTF8 = "UTF-8";

    public static final int DEFAULT_LIMIT = 20;
    public static final int DEFAULT_OFFSET = 0;
    public static final int DEFAULT_MAX_SEEK_BAR = 100;
    public static final int ERROR_DURATION = -1;
    public static final int CONNECTION_TIME_OUT = 5000;
    public static final int READ_INPUT_TIME_OUT = 5000;
    public static final int SPLASH_SCREEN_DISPLAY_LENGTH = 2000;
    public static final int DEFAULT_LIMIT_SEARCH = 30;
    public static final int DEFAULT_LIMIT_MORE = 50;

    public static final String BUNDLE_LIST_MUSIC_PLAY = "LIST_MUSIC_PLAY";
    public static final String BUNDLE_POSITION_SONG = "POSITION_SONG";
    public static final String INTENT_URL_DOWNLOAD = "url_download";
    public static final String INTENT_TITLE_DOWNLOAD = "title_download";
    public static final String INTENT_ACTION_DOWNLOAD = "ACTION_DOWNLOAD";
    public static final String EXTRAS_ID_GENRE = "id_genre";

    public static final String IMAGE_SIZE_ORIGIN = "original";
    public static final String IMAGE_SIZE_LARGE = "large";

    private Constants() {
    }
}
