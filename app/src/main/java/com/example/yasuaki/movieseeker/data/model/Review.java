package com.example.yasuaki.movieseeker.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Review implements Parcelable{

    @SerializedName("author")
    private String mAuthor;

    @SerializedName("content")
    private String mContent;

    public Review(){
    }

    protected Review(Parcel in) {
        mAuthor = in.readString();
        mContent = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthor);
        dest.writeString(mContent);
    }
}
