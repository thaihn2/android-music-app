package com.framgia.thaihn.tmusic.data.model;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class GenreSong {
    public static final String GENRE_ALL_MUSIC = "all-music";
    public static final String GENRE_ALL_AUDIO = "all-audio";
    public static final String GENRE_ALTERNATIVEROCK = "alternativerock";
    public static final String GENRE_AMBIENT = "ambient";
    public static final String GENRE_CLASSICAL = "classical";
    public static final String GENRE_COUNTRY = "country";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({GENRE_ALL_MUSIC, GENRE_ALL_AUDIO, GENRE_ALTERNATIVEROCK,
            GENRE_AMBIENT, GENRE_CLASSICAL, GENRE_COUNTRY})

    public @interface GenreAnotation {
    }

    @GenreAnotation
    static String[] genres = {GENRE_ALL_MUSIC, GENRE_ALL_AUDIO, GENRE_ALTERNATIVEROCK,
            GENRE_AMBIENT, GENRE_CLASSICAL, GENRE_COUNTRY};

    public static String[] getGenres() {
        return genres;
    }
}
