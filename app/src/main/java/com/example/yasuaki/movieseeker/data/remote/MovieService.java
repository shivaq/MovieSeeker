package com.example.yasuaki.movieseeker.data.remote;

import com.example.yasuaki.movieseeker.data.model.MovieResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Define the endpoints of the Movie DB. Retrofit converts HTTP API to java interface
 */
public interface MovieService {

    //Make them observable to be watched by observers
    @GET("movie/top_rated")
    Observable<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Observable<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

}
