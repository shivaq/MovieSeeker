package com.example.yasuaki.movieseeker.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.yasuaki.movieseeker.data.local.MovieDbHelper;
import com.example.yasuaki.movieseeker.data.local.MoviePersistenceContract;
import com.example.yasuaki.movieseeker.data.local.MoviePersistenceContract.MovieEntry;

public class MovieContentProvider extends ContentProvider {

    private final String TAG = MovieContentProvider.class.getSimpleName();

    public static final int CODE_MOVIES = 100;

    private MovieDbHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviePersistenceContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviePersistenceContract.PATH_MOVIE, CODE_MOVIES);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "inside insert");

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch(match){
            case CODE_MOVIES:
                long id = db.insertWithOnConflict(MovieEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                if(id > 0){
                    returnUri = MovieEntry.buildMovieUri();
                } else {
                    Log.d(TAG, "Failed to insert row into " + uri);
                    returnUri = null;
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
