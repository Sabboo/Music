package com.example.music.helpers;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static com.example.music.helpers.AppConstants.APPLICATION_JSON;
import static com.example.music.helpers.AppConstants.APP_JSON;
import static com.example.music.helpers.AppConstants.AUTHORIZATION;
import static com.example.music.helpers.AppConstants.BEARER;
import static com.example.music.helpers.AppConstants.CONTENT_TYPE;

/**
 * VolleyRequest.java
 * An helper class to executes the web service using the volley. Supported
 * Methods: GET and POST. By default it will be executed on POST method.
 */
public class VolleyRequestHelper {

    private static final String TAG = VolleyRequestHelper.class.getSimpleName();

    private Context context;
    private RequestQueue requestQueue;
    private OnRequestCompletedListener mRequestCompletedListener;

    public VolleyRequestHelper(Context context) {
        this.context = context;
    }

    /**
     * Used to call web service and get response as JSON using post method.
     *
     * @param context  - context of the activity.
     * @param callback - The callback reference.
     */
    public VolleyRequestHelper(Context context, OnRequestCompletedListener callback) {
        mRequestCompletedListener = callback;
        this.context = context;
    }

    /**
     * Request songs List response from the Web API.
     *
     * @param requestName   the String refers the request name
     * @param webserviceUrl the String refers the web service URL.
     * @param authToken     the String for the current user token
     * @param webMethod     the integer indicates the web method.
     */
    public void songsListRequestString(final String requestName,
                                       final String webserviceUrl,
                                       final String authToken,
                                       final int webMethod) {
        StringRequest stringRequest = new StringRequest(webMethod,
                webserviceUrl,
                response ->
                        mRequestCompletedListener.onRequestCompleted(
                                requestName, true, response, null)
                , error ->
                mRequestCompletedListener.onRequestCompleted(
                        requestName, false, null, error.getMessage())) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(CONTENT_TYPE, APP_JSON);
                headers.put(AUTHORIZATION, BEARER + authToken);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return APPLICATION_JSON;
            }

        };
        // Adding String request to request queue
        addToRequestQueue(stringRequest, requestName);
    }


    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    private <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(90 * 1000, 0, 1.0f));
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

    /**
     * To get the ImageLoader class instance to load the network image in Image
     * view.
     *
     * @return ImageLoader instance.
     */
    public ImageLoader getImageLoader() {
        return new ImageLoader(getRequestQueue(),
                new LruBitmapCache());
    }


}