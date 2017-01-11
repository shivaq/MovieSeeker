package com.example.yasuaki.movieseeker.data.local;

import android.net.Uri;
import android.provider.BaseColumns;

public final class MoviePersistenceContract {

    public static final String CONTENT_AUTHORITY = "com.example.yasuaki.movieseeker";
    private static final String CONTENT_SCHEME = "content://";
    private static final Uri BASE_CONTENT_URI = Uri.parse(CONTENT_SCHEME + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public MoviePersistenceContract() {
    }

    public static final class MovieEntry implements BaseColumns {

        static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE).build();

        public static final String TABLE_NAME = PATH_MOVIE;

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_FAVORITE = "is_favorite";

        public static Uri buildMovieUri() {
            return CONTENT_URI.buildUpon().build();
        }
    }

    public static final String[] MOVIE_PROJECTION = {
            MovieEntry.COLUMN_MOVIE_ID,
            MovieEntry.COLUMN_TITLE,
            MovieEntry.COLUMN_POSTER_PATH,
            MovieEntry.COLUMN_OVERVIEW,
            MovieEntry.COLUMN_RELEASE_DATE,
            MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieEntry.COLUMN_FAVORITE
    };

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_TITLE = 1;
    public static final int INDEX_POSTER_PATH = 2;
    public static final int INDEX_OVERVIEW = 3;
    public static final int INDEX_RELEASE_DATE = 4;
    public static final int INDEX_VOTE_AVERAGE = 5;
    public static final int INDEX_FAVORITE = 6;
}
