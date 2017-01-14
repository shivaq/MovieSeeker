package com.example.yasuaki.movieseeker.data.model;

import com.google.gson.annotations.SerializedName;

public class Trailer {

    @SerializedName("key")
    private String mTrailerKey;

    @SerializedName("name")
    private String mTrailerTitle;

    @SerializedName("size")
    private int mTrailerSize;

    public Trailer(){
    }

    public String getTrailerKey() {
        return mTrailerKey;
    }
}
