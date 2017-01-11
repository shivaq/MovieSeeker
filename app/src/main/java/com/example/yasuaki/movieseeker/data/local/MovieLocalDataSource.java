package com.example.yasuaki.movieseeker.data.local;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.yasuaki.movieseeker.data.MovieDataSource;
import com.example.yasuaki.movieseeker.data.MovieValues;
import com.example.yasuaki.movieseeker.data.local.MoviePersistenceContract.MovieEntry;
import com.example.yasuaki.movieseeker.data.model.Movie;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MovieLocalDataSource implements MovieDataSource {

    private final String TAG = MovieLocalDataSource.class.getSimpleName();

    private static MovieLocalDataSource INSTANCE;
    private final BriteDatabase mDatabaseHelper;
    private final SqlBrite mSqlBrite;


    private MovieLocalDataSource(Context context) {
        MovieDbHelper mOpenHelper = new MovieDbHelper(context);
        mSqlBrite = new SqlBrite.Builder().build();
        mDatabaseHelper = mSqlBrite.wrapDatabaseHelper(mOpenHelper, Schedulers.io());
    }

    //Do query in local
    public Observable<Movie> getMovie(String movieId) {

        String[] projection = MoviePersistenceContract.MOVIE_PROJECTION;
        String sqlQueryMovieTable = String.format("SELECT %s FROM %s WHERE %s LIKE ?",
                projection,
                MovieEntry.TABLE_NAME,
                MovieEntry.COLUMN_MOVIE_ID);

        return mDatabaseHelper.createQuery(MovieEntry.TABLE_NAME, sqlQueryMovieTable, movieId)
                .mapToOneOrDefault(MOVIE_MAPPER_FOR_QUERY, null);
    }

    //Pass Cursor and get Movie
    private final Func1<Cursor, Movie> MOVIE_MAPPER_FOR_QUERY = new Func1<Cursor, Movie>() {

        @Override
        public Movie call(Cursor cursor) {//Pass cursor and...

            int movieId = cursor.getInt(MoviePersistenceContract.INDEX_MOVIE_ID);
            String title = cursor.getString(MoviePersistenceContract.INDEX_TITLE);
            String thumbnailPath = cursor.getString(MoviePersistenceContract.INDEX_POSTER_PATH);
            String overView = cursor.getString(MoviePersistenceContract.INDEX_OVERVIEW);
            String releaseDate = cursor.getString(MoviePersistenceContract.INDEX_RELEASE_DATE);
            float voteAverage = cursor.getFloat(MoviePersistenceContract.INDEX_VOTE_AVERAGE);
            boolean isFavorite = cursor.getInt(MoviePersistenceContract.INDEX_FAVORITE) != 0;

            //Get movie.
            return new Movie(movieId, title, thumbnailPath, overView,
                    releaseDate, voteAverage, isFavorite);
        }
    };


    public static MovieLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MovieLocalDataSource(context);
        }
        return INSTANCE;
    }

    public void insertMovie(Movie movie) {
        ContentValues values = MovieValues.movieToContentValues(movie);
        Log.d(TAG, "insertMovie " + values.toString());
        mDatabaseHelper.insert(
                MovieEntry.TABLE_NAME,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }


    public void deleteMovie(String movieId) {
        String selection = MovieEntry.COLUMN_MOVIE_ID + " LIKE ?";
        String[] selectionArgs = {movieId};
        Log.d(TAG, movieId);

        mDatabaseHelper.delete(MovieEntry.TABLE_NAME,
                selection,
                selectionArgs);
    }


//    public Observable<Cursor> getMovie(String movieId) {
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
