package com.example.yasuaki.movieseeker.data.local;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yasuaki.movieseeker.data.MovieContentValues;
import com.example.yasuaki.movieseeker.data.local.MoviePersistenceContract.MovieEntry;
import com.example.yasuaki.movieseeker.data.model.Movie;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MovieLocalDataSource {

    private final String TAG = MovieLocalDataSource.class.getSimpleName();

    private static MovieLocalDataSource INSTANCE;
    private final BriteDatabase mDatabaseHelper;

    private static final String SQL_QUERY_FAVORITE_MOVIE = "SELECT * FROM "
            + MovieEntry.TABLE_NAME
            + " WHERE "
            + MovieEntry.COLUMN_FAVORITE
            + " = ?";

    private static final String SQL_QUERY_WITH_MOVIE_ID = "SELECT * FROM "
            + MovieEntry.TABLE_NAME
            + " WHERE "
            + MovieEntry.COLUMN_MOVIE_ID
            + " = ?";

    private MovieLocalDataSource(Context context) {
        MovieDbHelper mOpenHelper = new MovieDbHelper(context);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(mOpenHelper, Schedulers.io());
    }

    // Prevent direct instantiation.
    public static MovieLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MovieLocalDataSource(context);
        }
        return INSTANCE;
    }

    public void insertMovie(Movie movie) {
        ContentValues values = MovieContentValues.movieToContentValues(movie);
        mDatabaseHelper.insert(
                MovieEntry.TABLE_NAME,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void deleteMovie(String movieId) {
        String selection = MovieEntry.COLUMN_MOVIE_ID + " LIKE ?";
        String[] selectionArgs = {movieId};

        mDatabaseHelper.delete(MovieEntry.TABLE_NAME,
                selection,
                selectionArgs);
    }

    public Observable<Cursor> getFavoriteMovie() {
        return mDatabaseHelper.createQuery(
                MovieEntry.TABLE_NAME, SQL_QUERY_FAVORITE_MOVIE, "true")
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        return query.run();
                    }
                });
    }

    public Observable<Cursor> getMovieWithId(String movieId) {
        return mDatabaseHelper.createQuery(
                MovieEntry.TABLE_NAME, SQL_QUERY_WITH_MOVIE_ID, movieId)
                .map(new Func1<SqlBrite.Query, Cursor>() {
                    @Override
                    public Cursor call(SqlBrite.Query query) {
                        return query.run();
                    }
                });
    }
}
