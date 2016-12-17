package com.example.yasuaki.movieseeker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Data model to hold movie data
 */
public class Movie implements Parcelable {

    //TODO: JSON 変換を完全に GSON に任せる
    @SerializedName("poster_path")
    private String mThumbnailPath;

    @SerializedName("overview")
    private String mMovieOverView;

    @SerializedName("original_title")
    private String mMovieTitle;

    @SerializedName("release_date")
    private String mReleaseDate;

    @SerializedName("vote_average")
    private double mRating;

    public Movie(String thumbnailPath, String movieOverView, String movieTitle, String releaseDate, long rating) {
        mThumbnailPath = thumbnailPath;
        mMovieOverView = movieOverView;
        mMovieTitle = movieTitle;
        mReleaseDate = releaseDate;
        mRating = rating;
    }

    private Movie(Parcel in) {
        mThumbnailPath = in.readString();
        mMovieOverView = in.readString();
        mMovieTitle = in.readString();
        mReleaseDate = in.readString();
        mRating = in.readLong();
    }

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
        parcel.writeDouble(mRating);
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

    public String getThumbnailPath() {
        return mThumbnailPath;
    }

    public String getMovieTitle() {
        return mMovieTitle;
    }

    public String getMovieOverView() {
        return mMovieOverView;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public double getRating() {
        return mRating;
    }
}


