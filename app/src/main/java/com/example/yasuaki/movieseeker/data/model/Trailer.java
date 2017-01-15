package com.example.yasuaki.movieseeker.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Trailer implements Parcelable{

    @SerializedName("key")
    private String mTrailerKey;

    @SerializedName("name")
    private String mTrailerTitle;

    @SerializedName("size")
    private int mTrailerSize;

    public Trailer(){
    }

    protected Trailer(Parcel in) {
        mTrailerKey = in.readString();
        mTrailerTitle = in.readString();
        mTrailerSize = in.readInt();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public String getTrailerKey() {
        return mTrailerKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTrailerKey);
        dest.writeString(mTrailerTitle);
        dest.writeInt(mTrailerSize);
    }
}
