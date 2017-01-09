package com.example.yasuaki.movieseeker.data;

import android.content.ContentValues;

import com.example.yasuaki.movieseeker.data.local.MoviePersistenceContract.MovieEntry;
import com.example.yasuaki.movieseeker.data.model.Movie;

public class MovieValues {

    public static ContentValues from(Movie movie){
        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(MovieEntry.COLUMN_TITLE, movie.getMovieTitle());
        values.put(MovieEntry.COLUMN_POSTER_PATH, movie.getThumbnailPath());
        values.put(MovieEntry.COLUMN_OVERVIEW, movie.getMovieOverView());
        values.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getRating());
        values.put(MovieEntry.COLUMN_FAVORITE, Boolean.toString(movie.isFavorite()));
        return values;
    }
}
