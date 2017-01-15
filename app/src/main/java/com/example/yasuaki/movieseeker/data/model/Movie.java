package com.example.yasuaki.movieseeker.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Data model to hold movie data
 */
public final class Movie implements Parcelable {

    @SerializedName("id")
    private int mMovieId;

    @SerializedName("original_title")
    private String mMovieTitle;

    @SerializedName("poster_path")
    private String mThumbnailPath;

    @SerializedName("backdrop_path")
    private String mBackdropPath;

    @SerializedName("overview")
    private String mMovieOverView;

    @SerializedName("release_date")
    private String mReleaseDate;

    @SerializedName("vote_average")
    private float mVoteAverage;

    private boolean mIsFavorite;

    public Movie(int movieId, String title, String thumbnailPath, String backdropPath,
                 String overView, String releaseDate, float voteAverage,
                 boolean isFavorite) {
        mMovieId = movieId;
        mMovieTitle = title;
        mThumbnailPath = thumbnailPath;
        mBackdropPath = backdropPath;
        mMovieOverView = overView;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
        mIsFavorite = isFavorite;
    }


    protected Movie(Parcel in) {
        mMovieId = in.readInt();
        mMovieTitle = in.readString();
        mThumbnailPath = in.readString();
        mBackdropPath = in.readString();
        mMovieOverView = in.readString();
        mReleaseDate = in.readString();
        mVoteAverage = in.readFloat();
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


    public String getThumbnailPath() {
        return mThumbnailPath;
    }

    public String getBackdropPath() {
        return mBackdropPath;
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

    public float getVoteAverage() {
        return mVoteAverage;
    }

    public int getMovieId() {
        return mMovieId;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.mIsFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMovieId);
        dest.writeString(mMovieTitle);
        dest.writeString(mThumbnailPath);
        dest.writeString(mBackdropPath);
        dest.writeString(mMovieOverView);
        dest.writeString(mReleaseDate);
        dest.writeFloat(mVoteAverage);
        dest.writeByte((byte) (mIsFavorite ? 1 : 0));
    }
}


