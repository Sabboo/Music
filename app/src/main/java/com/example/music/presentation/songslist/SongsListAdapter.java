package com.example.music.presentation.songslist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.R;
import com.example.music.model.Song;
import com.example.music.presentation.songslist.SongsListAdapter.DownloadImageTask.ImageDownloaderListener;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.music.helpers.AppConstants.SONG_ITEM_TYPE;

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.SongItemViewHolder> {

    private Context context;
    private List<Song> songsList;
    private final SongItemClickListener songsItemClickListener;
    // Map used to store each item in the recyclerView with its correspond loaded bitmap
    // (Bitmap caching mechanism)
    private HashMap<String, SoftReference<Bitmap>> imagesCache = new HashMap<>();

    SongsListAdapter(Context context, List<Song> songsList, SongItemClickListener listener) {
        this.context = context;
        this.songsList = songsList;
        this.songsItemClickListener = listener;
    }

    @NonNull
    @Override
    public SongItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);

        // We will get the screen width to make each item takes half of the screen width
        // and it's height will be the same width plus the const value
        int screenWidth = getScreenWidth();

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = screenWidth / 2 + 150;
        layoutParams.width = screenWidth / 2;
        view.setLayoutParams(layoutParams);

        return new SongItemViewHolder(view);
    }

    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    @Override
    public void onBindViewHolder(@NonNull SongItemViewHolder holder, int position) {
        Song song = songsList.get(position);

        holder.itemTitleTextView.setText(song.getTitle());
        holder.itemArtistTextView.setText(song.getMainArtist().getName());

        if (song.getType().equals(SONG_ITEM_TYPE))
            holder.itemTypeImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_song));
        else
            holder.itemTypeImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_album));

        switchImageProgressState(holder, VISIBLE, INVISIBLE);

/*        ImageLoader imageLoader = new VolleyRequestHelper(context).getImageLoader();
        holder.itemImageView.setImageUrl(song.getCover().getSmall(), imageLoader);*/

        // Check for cached bitmaps before trying to request them again
        if (imagesCache.containsKey(String.valueOf(position)) && imagesCache.get(String.valueOf(position)).get() != null) {
            holder.itemImageView.setImageBitmap(imagesCache.get(String.valueOf(position)).get());
            switchImageProgressState(holder, INVISIBLE, VISIBLE);
        } else
            new DownloadImageTask(new ImageDownloaderListener() {
                @Override
                public void onImageDownloaded(Bitmap bitmap) {
                    holder.itemImageView.setImageBitmap(bitmap);
                    imagesCache.put(String.valueOf(position), new SoftReference<>(bitmap));
                    switchImageProgressState(holder, INVISIBLE, VISIBLE);
                }

                @Override
                public void onImageDownloadError() {
                    switchImageProgressState(holder, INVISIBLE, VISIBLE);
                    holder.itemImageView.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.ic_broken_image));
                    Log.e("onImageDownloadError", song.getCover().getSmall());
                }
            }).execute(song.getCover().getSmall());

        // Listener for song item click
        holder.itemView.setOnClickListener(v ->
                songsItemClickListener.onSongClick(song));
    }

    private void switchImageProgressState(@NonNull SongItemViewHolder holder, int imageProgressStatus, int imageItemStatus) {
        holder.itemImageProgress.setVisibility(imageProgressStatus);
        holder.itemImageView.setVisibility(imageItemStatus);
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    void replaceAllSongs(List<Song> songs) {
        this.songsList = null;
        this.imagesCache = new HashMap<>();
        this.songsList = songs;
        notifyDataSetChanged();
    }

    class SongItemViewHolder extends RecyclerView.ViewHolder {
        final ImageView itemImageView;
        final ImageView itemTypeImageView;
        final TextView itemTitleTextView;
        final TextView itemArtistTextView;
        final FrameLayout itemImageProgress;

        SongItemViewHolder(View view) {
            super(view);
            this.itemImageView = view.findViewById(R.id.iv_song);
            this.itemTypeImageView = view.findViewById(R.id.iv_song_type);
            this.itemTitleTextView = view.findViewById(R.id.tv_title);
            this.itemArtistTextView = view.findViewById(R.id.tv_artist);
            this.itemImageProgress = view.findViewById(R.id.fl_image_progress);
        }
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        public interface ImageDownloaderListener {
            void onImageDownloaded(final Bitmap bitmap);

            void onImageDownloadError();
        }

        private ImageDownloaderListener listener;

        DownloadImageTask(final ImageDownloaderListener listener) {
            this.listener = listener;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            final String url = urls[0];
            Bitmap bitmap = null;

            try {
                final InputStream inputStream = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (final MalformedURLException malformedUrlException) {
                malformedUrlException.printStackTrace();
            } catch (final IOException ioException) {
                ioException.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap downloadedBitmap) {
            if (null != downloadedBitmap) {
                listener.onImageDownloaded(downloadedBitmap);
            } else {
                listener.onImageDownloadError();
            }
        }
    }

}


