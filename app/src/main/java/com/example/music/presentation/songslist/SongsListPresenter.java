package com.example.music.presentation.songslist;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.example.music.helpers.AppConstants;
import com.example.music.helpers.OnRequestCompletedListener;
import com.example.music.helpers.VolleyRequestHelper;
import com.example.music.model.Song;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import static com.example.music.helpers.AppConstants.ALBUM_ITEM_TYPE;
import static com.example.music.helpers.AppConstants.ENCODING_UTF_8;
import static com.example.music.helpers.AppConstants.SONG_ITEM_TYPE;

public class SongsListPresenter implements SongsListContract.Presenter {

    private SongsListContract.View mSongsListView;
    private VolleyRequestHelper volleyRequestHelper;

    SongsListPresenter(Context context, @NonNull SongsListContract.View view) {
        this.mSongsListView = view;

        mSongsListView.setPresenter(this);

        /* The request completed listener triggers when on success for the request execute */
        OnRequestCompletedListener requestCompletedListener =
                (requestName, status, response, errorMessage) -> {
                    mSongsListView.setLoadingIndicator(false);
                    if (status) {
                        Gson gson = new Gson();
                        List<Song> songsList = gson.fromJson(response, new TypeToken<List<Song>>() {
                        }.getType());

                        if (songsList.size() == 0)
                            mSongsListView.showEmptySongsList();
                        else {
                            // Before passing the result to the view, we need to make sure that all of the
                            // response list are of the two types songs or releases
                            for (Iterator<Song> iterator = songsList.iterator(); iterator.hasNext(); ) {
                                Song song = iterator.next();
                                String type = song.getType();
                                if (!type.equals(SONG_ITEM_TYPE) && !type.equals(ALBUM_ITEM_TYPE)) {
                                    iterator.remove();
                                }
                            }
                            mSongsListView.showSongs(songsList);
                        }
                    } else {
                        mSongsListView.showNoInternetConnectionView();
                    }

                };
        volleyRequestHelper = new VolleyRequestHelper(context, requestCompletedListener);
    }

    @Override
    public void loadSongs(String query, String token) {
        try {
            // Trying to encode the given query to make it valid url request
            query = URLEncoder.encode(query, ENCODING_UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            // To remove all white spaces from given query
            query = query.replaceAll("\\s+", "");
            mSongsListView.showToast("UnsupportedEncodingException - Bad Encoding at loadSongs - Presenter");
        }
        mSongsListView.setLoadingIndicator(true);
        volleyRequestHelper.songsListRequestString(AppConstants.REQUEST_SONGS,
                String.format(AppConstants.URL_SONGS, query),
                token,
                Request.Method.GET);
    }

    @Override
    public void unsubscribe() {
        mSongsListView = null;
        volleyRequestHelper.cancelPendingRequests(AppConstants.REQUEST_SONGS);
    }
}
