package com.example.music.presentation.updatetoken;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.example.music.helpers.AppConstants;
import com.example.music.helpers.OnRequestCompletedListener;
import com.example.music.helpers.SharedPreferenceHelper;
import com.example.music.helpers.VolleyRequestHelper;
import com.example.music.model.TokenApiResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UpdateTokenPresenter implements UpdateTokenContract.Presenter {

    private UpdateTokenContract.View mView;
    private VolleyRequestHelper volleyRequestHelper;

    UpdateTokenPresenter(Context context, @NonNull UpdateTokenContract.View view) {
        this.mView = view;

        mView.setPresenter(this);

        /* The request completed listener triggers when on success for the request execute */
        OnRequestCompletedListener requestCompletedListener =
                (requestName, status, response, errorMessage) -> {
                    mView.setLoadingIndicator(false);
                    if (status) {
                        Gson gson = new Gson();
                        TokenApiResponse tokenApiResponse = gson.fromJson(response, new TypeToken<TokenApiResponse>() {
                        }.getType());
                        if (tokenApiResponse.getAccessToken() != null) {
                            SharedPreferenceHelper.setSomeStringValue(context, tokenApiResponse.getAccessToken());
                            mView.tokenUpdatedSuccessfully();
                        }
                    } else {
                        mView.showNoInternetConnectionView();
                    }

                };

        volleyRequestHelper = new VolleyRequestHelper(context, requestCompletedListener);
    }

    @Override
    public void updateToken() {
        mView.setLoadingIndicator(true);
        volleyRequestHelper.updateTokenRequestString(AppConstants.REQUEST_UPDATE_TOKEN_NAME,
                AppConstants.URL_TOKEN,
                Request.Method.POST);
    }

    @Override
    public void unsubscribe() {
        mView = null;
        volleyRequestHelper.cancelPendingRequests(AppConstants.REQUEST_UPDATE_TOKEN_NAME);
    }
}
