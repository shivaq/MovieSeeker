package com.example.yasuaki.movieseeker.data.local;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviePersistenceContract {

    public static final String CONTENT_AUTHORITY = "com.example.yasuaki.movieseeker";
    private static final String CONTENT_SCHEME = "content://";
    public static final Uri BASE_CONTENT_URI = Uri.parse(CONTENT_SCHEME + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_REVIEW = "review";

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

//        public static String[] MOVIE_COLUMNS = new String[]{
//                MoviePersistenceContract.MovieEntry.COLUMN_MOVIE_ID,
//                MoviePersistenceContract.MovieEntry.COLUMN_TITLE,
//                MoviePersistenceContract.MovieEntry.COLUMN_POSTER_PATH,
//                MoviePersistenceContract.MovieEntry.COLUMN_OVERVIEW,
//                MoviePersistenceContract.MovieEntry.COLUMN_RELEASE_DATE,
//                MoviePersistenceContract.MovieEntry.COLUMN_VOTE_AVERAGE,
//                MoviePersistenceContract.MovieEntry.COLUMN_FAVORITE
//        };

        public static Uri buildMovieUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
        }
    }

    public static final class TrailerEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TRAILER).build();

        public static final String TABLE_NAME = PATH_TRAILER;

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_YOUTUBE_ID = "youtube_id";

        public static Uri buildTrailerUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
        }
    }

    public static final class ReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_REVIEW).build();

        public static final String TABLE_NAME = PATH_REVIEW;

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_REVIEW_CONTENT = "review_content";

        public static Uri buildReviewUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
        }
    }
}
