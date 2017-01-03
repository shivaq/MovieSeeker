package com.example.yasuaki.movieseeker.util;

import android.net.Uri;
import android.util.Log;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String IMAGE_FETCH_BASE_URL = "https://image.tmdb.org/t/p/";

    private static final String YOUTUBE_URL = "https://www.youtube.com/";
//    watch?v={key}
    /**
     * Builds the URL to fetch the movie thumbnail
     */
    public static Uri buildUriForThumbnail(String thumbnailPath) {

        String fetchedImageSize = "w185";

        Uri builtUriForImage = Uri.parse(IMAGE_FETCH_BASE_URL).buildUpon()
                .appendPath(fetchedImageSize)
                .appendEncodedPath(thumbnailPath)
                .build();

        Log.d(TAG, "Built URI for thumbnail " + builtUriForImage);
        return builtUriForImage;
    }

    public static Uri buildUriForTrailer(String trailerKey){

        Uri trailerUri = Uri.parse(YOUTUBE_URL).buildUpon()
                .appendPath("watch")
                .appendQueryParameter("v", trailerKey)
                .build();
        Log.d(TAG, "Built URI for trailer " + trailerUri);

        return trailerUri;
    }
}
