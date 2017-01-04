package com.example.yasuaki.movieseeker.data.remote;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    private static final String TAG = ServiceFactory.class.getSimpleName();

    private static final String BASE_MOVIE_DB_URL = "http://api.themoviedb.org/3/";
//    private static final String BASE_YOUTUBE_URL = "https://img.youtube.com/";

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
                .addInterceptor(logging)
                .build();

//        if(isMovieDbRequest){
//            baseUrl = BASE_MOVIE_DB_URL;
//        } else {
//            baseUrl =BASE_YOUTUBE_URL;
//        }

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
