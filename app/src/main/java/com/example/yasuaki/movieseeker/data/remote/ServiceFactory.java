package com.example.yasuaki.movieseeker.data.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    private static final String TAG = ServiceFactory.class.getSimpleName();

    private static final String BASE_MOVIE_DB_URL = "http://api.themoviedb.org/3/";

    public ServiceFactory(){}

    /**
     * Generate MovieService implementation with Retrofit
     * @return implemented MovieService interface
     */
    public static MovieService makeMovieService(){

        String baseUrl;

        //Create logging interceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //Create OkHttpClient and add interceptor just for http logging.
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(logging)
                .build();

        //Create Retrofit instance with base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_MOVIE_DB_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        //Generate MovieService implementation with Retrofit
        return retrofit.create(MovieService.class);
    }
}
