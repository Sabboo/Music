package com.example.music.presentation.songslist;

import com.example.music.model.Song;
import com.example.music.presentation.BasePresenter;
import com.example.music.presentation.BaseView;

import java.util.List;

public interface SongsListContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showSongs(List<Song> songsList);

        void showNoInternetConnectionView();

        void hideNoInternetConnectionView();

        void showToast(String message);

        void showEmptySongsList();

    }

    interface Presenter extends BasePresenter {

        void loadSongs(String query, String token);

    }

}
