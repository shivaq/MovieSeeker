package com.example.yasuaki.movieseeker.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Data model to hold movie data
 */
public class Movie implements Parcelable {

    @SerializedName("poster_path")
    private String mThumbnailPath;

    @SerializedName("overview")
    private String mMovieOverView;

    @SerializedName("original_title")
    private String mMovieTitle;

    @SerializedName("release_date")
    private String mReleaseDate;

    @SerializedName("vote_average")
    private float mRating;

    @SerializedName("id")
    private int mId;

    private String mTrailerKey;


    private boolean mIsFavorite;

    protected Movie(Parcel in) {
        mThumbnailPath = in.readString();
        mMovieOverView = in.readString();
        mMovieTitle = in.readString();
        mReleaseDate = in.readString();
        mRating = in.readFloat();
        mId = in.readInt();
        mIsFavorite = in.readByte() != 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mThumbnailPath);
        parcel.writeString(mMovieOverView);
        parcel.writeString(mMovieTitle);
        parcel.writeString(mReleaseDate);
        parcel.writeFloat(mRating);
        parcel.writeInt(mId);
        parcel.writeByte((byte) (mIsFavorite ? 1 : 0));
    }

    public String getThumbnailPath() {
        return mThumbnailPath;
    }

    public String getMovieOverView() {
        return mMovieOverView;
    }

    public String getMovieTitle() {
        return mMovieTitle;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public float getRating() {
        return mRating;
    }

    public int getId() {
        return mId;
    }


    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.mIsFavorite = favorite;
    }

    public String getTrailerKey() {
        return mTrailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        mTrailerKey = trailerKey;
    }
}


