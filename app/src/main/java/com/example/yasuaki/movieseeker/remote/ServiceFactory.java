package com.example.yasuaki.movieseeker.remote;

import android.net.Uri;
import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    private static final String TAG = ServiceFactory.class.getSimpleName();

    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    private static final String IMAGE_FETCH_BASE_URL = "https://image.tmdb.org/t/p/";
    private static String fetchedImageSize = "w185";

    public ServiceFactory(){}

    public static MovieService makeMovieService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(MovieService.class);
    }

    /**
     * Builds the URL to fetch the movie thumbnail
     */
    public static Uri buildUrlForThumbnail(String thumbnailPath) {
        Uri builtUriForImage = Uri.parse(IMAGE_FETCH_BASE_URL).buildUpon()
                .appendPath(fetchedImageSize)
                .appendEncodedPath(thumbnailPath)
                .build();

        Log.d(TAG, "Built URI for thumbnail " + builtUriForImage);
        return builtUriForImage;
    }
}
