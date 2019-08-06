package com.example.music.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceHelper {

    private static final String PREF_KEY_ACCESS_TOKEN = "access-token";

    private SharedPreferenceHelper() {
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getPrefKeyAccessToken(Context context) {
        return getSharedPreferences(context).getString(PREF_KEY_ACCESS_TOKEN, null);
    }

    public static void setSomeStringValue(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_KEY_ACCESS_TOKEN, newValue);
        editor.apply();
    }
}
