package com.example.yasuaki.movieseeker.data.local;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.util.Log;

import com.example.yasuaki.movieseeker.data.MovieValues;
import com.example.yasuaki.movieseeker.data.model.Movie;

public class MovieLocalDataSource {

    private final String TAG = MovieLocalDataSource.class.getSimpleName();
    private static MovieLocalDataSource INSTANCE;

    private ContentResolver mContentResolver;

    // Prevent direct instantiation.
    private MovieLocalDataSource(ContentResolver contentResolver){
        mContentResolver = contentResolver;
    }

    public static MovieLocalDataSource getInstance(ContentResolver contentResolver){
        if(INSTANCE == null){
            INSTANCE = new MovieLocalDataSource(contentResolver);
        }
        return INSTANCE;
    }

    public void saveMovie(Movie movie){
        ContentValues values = MovieValues.from(movie);
        Log.d(TAG, "saveMovie " + values.toString());
        mContentResolver.insert(MoviePersistenceContract.MovieEntry.buildMovieUri(),
                values);
    }


    public void deleteMovie(String movieId){
        String selection = MoviePersistenceContract.MovieEntry.COLUMN_MOVIE_ID + " LIKE ?";
        String[] selectionArgs = {movieId};
        Log.d(TAG, movieId);
        mContentResolver.delete(
                MoviePersistenceContract.MovieEntry.buildMovieUri(),
                selection,
                selectionArgs);
    }

    //TODO:Get Movie from DB

}
