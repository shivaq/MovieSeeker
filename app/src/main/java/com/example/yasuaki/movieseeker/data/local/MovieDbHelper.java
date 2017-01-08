package com.example.yasuaki.movieseeker.data.local;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yasuaki.movieseeker.data.local.MoviePersistenceContract.MovieEntry;
import com.example.yasuaki.movieseeker.data.local.MoviePersistenceContract.TrailerEntry;
import com.example.yasuaki.movieseeker.data.local.MoviePersistenceContract.ReviewEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";

    public static final int DATABASE_VERSION = 1;

    public static final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
            MovieEntry.TABLE_NAME + " (" +
            MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
            MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_RELEASE_DATE + " DATE NOT NULL, " +
            MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
            MovieEntry.COLUMN_FAVORITE + " BOOLEAN DEFAULT 'FALSE', " +
            " )";

    public static final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " +
            TrailerEntry.TABLE_NAME + " (" +
            TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TrailerEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
            TrailerEntry.COLUMN_YOUTUBE_ID + " INTEGER NOT NULL, " +
            " )";

    public static final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " +
            ReviewEntry.TABLE_NAME + " (" +
            ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
            ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
            ReviewEntry.COLUMN_REVIEW_CONTENT + " TEXT NOT NULL, " +
            " )";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_TRAILER_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        onCreate(db);
    }
}
