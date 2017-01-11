package com.example.yasuaki.movieseeker.data.remote;

import com.example.yasuaki.movieseeker.data.model.MovieResponse;
import com.example.yasuaki.movieseeker.data.model.ReviewResponse;
import com.example.yasuaki.movieseeker.data.model.TrailerResponse;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Define the endpoints of the Movie DB. Retrofit converts HTTP API to java interface
 */
public interface MovieRemoteDataSource {

    //Make them observable to be watched by observers
    @GET("movie/top_rated")
    Observable<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Observable<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Observable<TrailerResponse> getMovieTrailer(@Path("id") int id,
                                                @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Observable<ReviewResponse> getReview(@Path("id") int id,
                                          @Query("api_key") String apiKey);


    /******** Factory class that sets up  a service to access the Movie DB *******/
    class ServiceFactory {

        private static final String BASE_MOVIE_DB_URL = "http://api.themoviedb.org/3/";

        public ServiceFactory(){}

        /**
         * Generate MovieRemoteDataSource implementation with Retrofit
         * @return implemented MovieRemoteDataSource interface
         */
        public static MovieRemoteDataSource makeMovieService(){

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

            //Generate MovieRemoteDataSource implementation with Retrofit
            return retrofit.create(MovieRemoteDataSource.class);
        }
    }
}
