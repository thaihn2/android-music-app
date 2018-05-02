package com.framgia.thaihn.tmusic.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

public final class Utils {

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
