package com.example.yasuaki.movieseeker.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Data model to hold movie data
 */
public class Movie implements Parcelable {

    private String mThumbnailPath;
    private String mMovieOverView;
    private String mMovieTitle;
    private String mReleaseDate;
    private long mRating;

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
        parcel.writeLong(mRating);
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

    public long getRating() {
        return mRating;
    }
}


