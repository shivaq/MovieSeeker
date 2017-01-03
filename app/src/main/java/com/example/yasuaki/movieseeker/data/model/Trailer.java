package com.example.yasuaki.movieseeker.data.model;

import com.google.gson.annotations.SerializedName;

public class Trailer {

    @SerializedName("key")
    String mTrailerKey;

    @SerializedName("name")
    String mTrailerTitle;

    @SerializedName("size")
    int mTrailerSize;

    public Trailer(){

    }

}
