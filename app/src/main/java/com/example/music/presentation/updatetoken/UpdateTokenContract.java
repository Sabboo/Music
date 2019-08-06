package com.example.music.presentation.updatetoken;

import com.example.music.presentation.BasePresenter;
import com.example.music.presentation.BaseView;

public interface UpdateTokenContract {

    interface View extends BaseView<UpdateTokenContract.Presenter> {

        void setLoadingIndicator(boolean active);

        void showNoInternetConnectionView();

        void hideNoInternetConnectionView();

        void showToast(String message);

        void tokenUpdatedSuccessfully();
    }

    interface Presenter extends BasePresenter {

        void updateToken();

    }
}
