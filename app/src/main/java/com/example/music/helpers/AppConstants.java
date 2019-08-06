package com.example.music.helpers;

public class AppConstants {

    static final String GATEWAY_KEY = "Ge6c853cf-5593-a196-efdb-e3fd7b881eca";
    private static final String URL_BASE_ENDPOINT = "http://staging-gateway.mondiamedia.com";
    private static final String URL_ENDPOINT_V2 = "/v2/api";
    private static final String URL_ENDPOINT_V0 = "/v0/api";
    public static final String URL_TOKEN = URL_BASE_ENDPOINT + URL_ENDPOINT_V0 + "/gateway/token/client";
    public static final String URL_SONGS = URL_BASE_ENDPOINT + URL_ENDPOINT_V2 + "/sayt/flat?query=%1$s";

    public static final String REQUEST_SONGS_NAME = "songs";
    public static final String REQUEST_UPDATE_TOKEN_NAME = "songs";

    static final String APPLICATION_JSON = "application/json; charset=utf-8";
    static final String APP_JSON = "application/json";
    static final String APP_FORM = "application/x-www-form-urlencoded";
    static final String CONTENT_TYPE_HEADER = "Content-Type";
    static final String GATEWAY_KEY_HEADER = "X-MM-GATEWAY-KEY";
    static final String AUTHORIZATION = "Authorization";
    static final String BEARER = "Bearer ";
    public static final String ENCODING_UTF_8 = "UTF-8";

    public static final String SONG_ITEM_TYPE = "song";
    public static final String ALBUM_ITEM_TYPE = "release";

    private AppConstants() {
    }

}
