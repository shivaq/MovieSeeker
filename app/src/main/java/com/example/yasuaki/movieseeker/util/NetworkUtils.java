package com.example.yasuaki.movieseeker.util;

import android.net.Uri;
import android.util.Log;

import com.example.yasuaki.movieseeker.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Yasuaki on 2016/12/11.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_DB_URL = "https://api.themoviedb.org/3/discover/movie?";
    private static final String MOVIE_BASE_URL = MOVIE_DB_URL;
    private static final String IMAGE_FETCH_BASE_URL = "https://image.tmdb.org/t/p/";

    private static final String SORT_PARAM = "sort_by";

    private static final String API_KEY = "api_key";

    static String fetcheImageSize = "w185";

    public static URL buildUrl(String sortOrder){
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter(SORT_PARAM, sortOrder)
                .appendQueryParameter(API_KEY, BuildConfig.OPEN_MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        Log.d(TAG, "Built URI " + url);
        return url;
    }

    public static Uri buildUrlForThumbnail(String thumbnailPath){
        Uri builtUriForImage = Uri.parse(IMAGE_FETCH_BASE_URL).buildUpon()
                .appendPath(fetcheImageSize)
                .appendEncodedPath(thumbnailPath)
                .build();

        Log.d(TAG, "Built URI for thumbnail " + builtUriForImage);
        return builtUriForImage;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

        try{
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if(hasInput){
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
