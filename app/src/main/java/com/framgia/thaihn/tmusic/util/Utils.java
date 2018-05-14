package com.framgia.thaihn.tmusic.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.framgia.thaihn.tmusic.BuildConfig;

public final class Utils {

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

    public static String calculatorDuration(int duration) {
        int hours = (int) (duration / (1000 * 60 * 60));
        int minutes = (int) (duration % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((duration % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (hours > 0) {
            return String.format("%d02:%d02%:%d02", hours, minutes, seconds);
        } else {
            return String.format("%d02:%d02", minutes, seconds);
        }
    }

    public static void setStringToPreferences(String key, String values, Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, values);
        editor.commit();
    }

    public static String getStringFromPreferences(String key, Context context) {
        return getSharedPreferences(context).getString(key, null);
    }

    public static boolean checkNetwork(Context context) {
        boolean available = false;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            available = true;
        }
        return available;
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private Utils() {
    }
}
