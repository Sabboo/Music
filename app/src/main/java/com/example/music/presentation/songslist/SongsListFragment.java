package com.example.music.presentation.songslist;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.R;
import com.example.music.databinding.FragmentSongsListBinding;
import com.example.music.helpers.ActivityUtils;
import com.example.music.helpers.SharedPreferenceHelper;
import com.example.music.model.Song;
import com.example.music.presentation.MainActivity;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class SongsListFragment extends Fragment implements SongsListContract.View, SongItemClickListener {

    private SongsListContract.Presenter mPresenter;
    private FragmentSongsListBinding mBinding;
    private RecyclerView.Adapter mAdapter;

    public SongsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SongsListPresenter(getContext(), this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_songs_list,
                container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());

        // Trying to restore previous search query in case fragment transactions happened.
        if (getActivity() != null) {
            String query = ((MainActivity) getActivity()).query;
            if (query != null)
                mPresenter.loadSongs(query, SharedPreferenceHelper.getPrefKeyAccessToken(getContext()));
        }

        if (mBinding.rvSongs.getLayoutManager() == null)
            mBinding.rvSongs.setLayoutManager(
                    new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleSearchEditTextQueries();
        handleNoInternetConnectionScreen();
    }

    private void handleSearchEditTextQueries() {
        mBinding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            ActivityUtils.hideSoftKeyboard(getActivity());
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                int searchTextLength = mBinding.etSearch.getText().length();
                if (searchTextLength > 1)
                    performSearch();
                else if (searchTextLength == 1)
                    showToast("More than one character is needed...");
                else
                    showToast("Can't search empty text...");
                return true;
            }
            return false;
        });
    }

    private void performSearch() {
        String searchQuery = mBinding.etSearch.getText().toString().trim();
        if (getActivity() != null)
            ((MainActivity) getActivity()).query = searchQuery;
        mPresenter.loadSongs(searchQuery, SharedPreferenceHelper.getPrefKeyAccessToken(getContext()));
    }

    private void handleNoInternetConnectionScreen() {
        mBinding.includeLayoutNoConnection.findViewById(R.id.btn_try_again).setOnClickListener(v -> {
            if (getContext() != null)
                if (ActivityUtils.isNetworkAvailable(getContext()))
                    hideNoInternetConnectionView();
        });
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mBinding.progressBarSongsList.setVisibility(active ? VISIBLE : GONE);
    }

    @Override
    public void showSongs(List<Song> songsList) {
        mBinding.includeLayoutEmptyList.setVisibility(GONE);
        mAdapter = mBinding.rvSongs.getAdapter();
        if (mAdapter == null) {
            SongsListAdapter songsListAdapter = new SongsListAdapter(getContext(), songsList, this);
            mBinding.rvSongs.setAdapter(songsListAdapter);
        } else
            ((SongsListAdapter) mAdapter).replaceAllSongs(songsList);
    }

    @Override
    public void showNoInternetConnectionView() {
        mBinding.includeLayoutNoConnection.setVisibility(VISIBLE);
        mBinding.etSearch.setVisibility(INVISIBLE);
        mBinding.rvSongs.setVisibility(INVISIBLE);
        mBinding.includeLayoutEmptyList.setVisibility(INVISIBLE);
    }

    @Override
    public void hideNoInternetConnectionView() {
        mBinding.includeLayoutNoConnection.setVisibility(INVISIBLE);
        mBinding.etSearch.setVisibility(VISIBLE);
        mBinding.rvSongs.setVisibility(VISIBLE);
    }

    @Override
    public void onSongClick(Song clickedSong) {
        if (getActivity() != null) {
            Navigation.findNavController(getActivity(), R.id.fragment_home)
                    .navigate(R.id.action_songsListFragment_to_songDetailsFragment);

        }
    }

    @Override
    public void showEmptySongsList() {
        mAdapter = mBinding.rvSongs.getAdapter();
        if (mAdapter == null) {
            SongsListAdapter songsListAdapter = new SongsListAdapter(getContext(), new ArrayList<>(), this);
            mBinding.rvSongs.setAdapter(songsListAdapter);
        } else
            ((SongsListAdapter) mAdapter).replaceAllSongs(new ArrayList<>());

        mBinding.includeLayoutEmptyList.setVisibility(VISIBLE);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(SongsListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

}
