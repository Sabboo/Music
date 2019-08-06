package com.example.music.helpers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.view.inputmethod.InputMethodManager;

public class ActivityUtils {

    /**
     * Helper method to force hiding keyboard in a given activity
     *
     * @param activity shouldn't be null
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(), 0);
        }

    }

    /**
     * Helper method to check for internet connection
     *
     * @param context shouldn't be null , the given context to test
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
