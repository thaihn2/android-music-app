package com.framgia.thaihn.tmusic.util;

public final class Constants {

    public static final String URL_BASE = "https://api-v2.soundcloud.com/";
    public static final String CONTENT_URL = "charts?kind=top&genre=soundcloud%3Agenres%3A";
    public static final String LIMIT = "limit";
    public static final String OFFSET = "offset";
    public static final String CLIENT_ID = "client_id";
    public static final String METHOD_GET = "GET";

    public static final int DEFAULT_LIMIT = 20;
    public static final int DEFAULT_OFFSET = 20;

    private Constants() {
    }
}
