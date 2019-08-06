package com.example.music.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Cover implements Parcelable {

    private String small;

    private String large;

    protected Cover(Parcel in) {
        small = in.readString();
        large = in.readString();
    }

    public static final Creator<Cover> CREATOR = new Creator<Cover>() {
        @Override
        public Cover createFromParcel(Parcel in) {
            return new Cover(in);
        }

        @Override
        public Cover[] newArray(int size) {
            return new Cover[size];
        }
    };

    public String getSmall() {
        if (small != null)
            return "http://" + small.substring(2);
        else
            return "";
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getLarge() {
        if (large != null)
            return "http://" + large.substring(2);
        else
            return "";
    }

    public void setLarge(String large) {
        this.large = large;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(small);
        dest.writeString(large);
    }
}
