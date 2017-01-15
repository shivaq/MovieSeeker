package com.example.yasuaki.movieseeker.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yasuaki.movieseeker.data.local.MoviePersistenceContract.MovieEntry;

class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";

    private static final int DATABASE_VERSION = 2;

    private static final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
            MovieEntry.TABLE_NAME + " (" +
            MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL PRIMARY KEY, " +
            MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_RELEASE_DATE + " DATE NOT NULL, " +
            MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
            MovieEntry.COLUMN_FAVORITE + " BOOLEAN DEFAULT 'FALSE'" +
            " )";

    MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
