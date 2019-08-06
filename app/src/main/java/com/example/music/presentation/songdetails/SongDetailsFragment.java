package com.example.music.presentation.songdetails;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.music.R;
import com.example.music.databinding.FragmentSongDetailsBinding;
import com.example.music.helpers.DownloadImageTask;
import com.example.music.model.Song;

public class SongDetailsFragment extends Fragment {

    public SongDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSongDetailsBinding mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_song_details,
                container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());

        if (getArguments() != null) {
            Song clickedSong = SongDetailsFragmentArgs.fromBundle(getArguments()).getSongArgument();

            populateImages(mBinding, clickedSong);
            mBinding.tvItemTitle.setText(clickedSong.getTitle());
            mBinding.tvItemArtist.setText(clickedSong.getMainArtist().getName());
            mBinding.tvItemType.setText(clickedSong.getType());
            mBinding.tvItemDate.setText(clickedSong.getPublishingDate());
        }

        return mBinding.getRoot();
    }

    private void populateImages(FragmentSongDetailsBinding mBinding, Song clickedSong) {
        new DownloadImageTask(new DownloadImageTask.ImageDownloaderListener() {
            @Override
            public void onImageDownloaded(Bitmap bitmap) {
                mBinding.ivItem.setImageBitmap(bitmap);
            }

            @Override
            public void onImageDownloadError() {
                mBinding.ivItem.setImageDrawable(
                        ContextCompat.getDrawable(mBinding.ivItem.getContext(), R.drawable.ic_broken_image));
                Log.e("onImageDownloadError", clickedSong.getCover().getLarge());
            }
        }).execute(clickedSong.getCover().getLarge());

        new DownloadImageTask(new DownloadImageTask.ImageDownloaderListener() {
            @Override
            public void onImageDownloaded(Bitmap bitmap) {
                mBinding.ivItemMinimized.setImageBitmap(bitmap);
            }

            @Override
            public void onImageDownloadError() {
                mBinding.ivItemMinimized.setImageDrawable(
                        ContextCompat.getDrawable(mBinding.ivItemMinimized.getContext(), R.drawable.ic_broken_image));
                Log.e("onImageDownloadError", clickedSong.getCover().getSmall());
            }
        }).execute(clickedSong.getCover().getSmall());
    }

}
