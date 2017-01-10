package com.example.yasuaki.movieseeker.data.local;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.yasuaki.movieseeker.data.MovieValues;
import com.example.yasuaki.movieseeker.data.model.Movie;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.schedulers.Schedulers;

public class MovieLocalDataSource {

    private final String TAG = MovieLocalDataSource.class.getSimpleName();

    private static MovieLocalDataSource INSTANCE;
    private final BriteDatabase mDatabaseHelper;

    private MovieLocalDataSource(Context context) {
        MovieDbHelper mOpenHelper = new MovieDbHelper(context);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(mOpenHelper, Schedulers.io());
    }

    public static MovieLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MovieLocalDataSource(context);
        }
        return INSTANCE;
    }

    public void saveMovie(Movie movie) {
        ContentValues values = MovieValues.from(movie);
        Log.d(TAG, "saveMovie " + values.toString());
        mDatabaseHelper.insert(
                MoviePersistenceContract.MovieEntry.TABLE_NAME,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void deleteMovie(String movieId) {
        String selection = MoviePersistenceContract.MovieEntry.COLUMN_MOVIE_ID + " LIKE ?";
        String[] selectionArgs = {movieId};
        Log.d(TAG, movieId);

        mDatabaseHelper.delete(MoviePersistenceContract.MovieEntry.TABLE_NAME,
                selection,
                selectionArgs);
    }

//    public Observable<Cursor> queryMovie(String movieId) {
//
//        String selection = MoviePersistenceContract.MovieEntry.COLUMN_MOVIE_ID + " LIKE ?";
//        String[] selectionArgs = {movieId};
//
//        return mContentResolver.query(
//                MoviePersistenceContract.MovieEntry.buildMovieUri(),
//                selection,
//                selectionArgs);


//        Observable<SqlBrite.Query> queriedMovie =
//                db.createQuery(
//                        MoviePersistenceContract.MovieEntry.TABLE_NAME,
//                        MovieDbHelper.SQL_QUERY_MOVIE_TABLE);
//
//        queriedMovie.subscribe(new Action1<SqlBrite.Query>() {
//            @Override
//            public void call(SqlBrite.Query query) {
//                Cursor cursor = query.run();
//            }
//        });
//    }


}
