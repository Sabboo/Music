package com.example.music.model;

public class Cover {

    private String small;

    public String getSmall() {
        if (small != null)
            return "http://" + small.substring(2);
        else
            return "";
    }

    public void setSmall(String small) {
        this.small = small;
    }
}
