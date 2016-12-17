package com.example.yasuaki.movieseeker.data.remote;

import android.net.Uri;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //Create OkHttpClient and add interceptor just for http logging.
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
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
