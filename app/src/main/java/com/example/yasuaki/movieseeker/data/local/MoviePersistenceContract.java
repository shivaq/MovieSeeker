package com.example.yasuaki.movieseeker.data.local;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviePersistenceContract {

    public static final String CONTENT_AUTHORITY = "com.example.yasuaki.movieseeker";
    private static final String CONTENT_SCHEME = "content://";
    public static final Uri BASE_CONTENT_URI = Uri.parse(CONTENT_SCHEME + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public MoviePersistenceContract() {
    }

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
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

}
