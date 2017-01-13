package com.example.yasuaki.movieseeker.util;

import android.net.Uri;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String IMAGE_FETCH_BASE_URL = "https://image.tmdb.org/t/p/";

    private static final String YOUTUBE_URL = "https://www.youtube.com/";
    private static final String YOUTUBE_THUMBNAIL = "https://img.youtube.com/";
//    watch?v={key}
    /**
     * Builds the URL to fetch the movie thumbnail
     */
    public static Uri buildUriForThumbnail(String thumbnailPath) {

        String fetchedImageSize = "w342";

        Uri builtUriForImage = Uri.parse(IMAGE_FETCH_BASE_URL).buildUpon()
                .appendPath(fetchedImageSize)
                .appendEncodedPath(thumbnailPath)
                .build();

        return builtUriForImage;
    }

    public static Uri buildUriForTrailer(String trailerKey){

        Uri trailerUri = Uri.parse(YOUTUBE_URL).buildUpon()
                .appendPath("watch")
                .appendQueryParameter("v", trailerKey)
                .build();

        return trailerUri;
    }

    public static Uri buildTrailerThumbnailUri(String trailerKey){

        Uri trailerThumbnailUri = Uri.parse(YOUTUBE_THUMBNAIL).buildUpon()
                .appendPath("vi")
                .appendPath(trailerKey)
                .appendPath("1.jpg")
                .build();

        return trailerThumbnailUri;
    }
}
