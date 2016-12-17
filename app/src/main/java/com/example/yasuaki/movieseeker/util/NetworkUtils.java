package com.example.yasuaki.movieseeker.util;

import android.net.Uri;
import android.util.Log;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String IMAGE_FETCH_BASE_URL = "https://image.tmdb.org/t/p/";

    /**
     * Builds the URL to fetch the movie thumbnail
     */
    public static Uri buildUrlForThumbnail(String thumbnailPath) {

        String fetchedImageSize = "w185";

        Uri builtUriForImage = Uri.parse(IMAGE_FETCH_BASE_URL).buildUpon()
                .appendPath(fetchedImageSize)
                .appendEncodedPath(thumbnailPath)
                .build();

        Log.d(TAG, "Built URI for thumbnail " + builtUriForImage);
        return builtUriForImage;
    }
}
