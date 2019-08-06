package com.example.music.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {

    private int id;

    private String type;

    private String title;

    private Artist mainArtist;

    private Cover cover;

    private String publishingDate;

    protected Song(Parcel in) {
        id = in.readInt();
        type = in.readString();
        title = in.readString();
        mainArtist = in.readParcelable(Artist.class.getClassLoader());
        cover = in.readParcelable(Cover.class.getClassLoader());
        publishingDate = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artist getMainArtist() {
        return mainArtist;
    }

    public void setMainArtist(Artist mainArtist) {
        this.mainArtist = mainArtist;
    }

    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public String getPublishingDate() {
        return publishingDate.substring(0, 10);
    }

    public void setPublishingDate(String publishingDate) {
        this.publishingDate = publishingDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeString(title);
        dest.writeParcelable(mainArtist, flags);
        dest.writeParcelable(cover, flags);
        dest.writeString(publishingDate);
    }
}
