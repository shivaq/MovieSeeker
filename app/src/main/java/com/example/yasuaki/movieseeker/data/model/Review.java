package com.example.yasuaki.movieseeker.data.model;

import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("author")
    String mAuthor;

    @SerializedName("content")
    String mContent;

    public Review(){

    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }
}
