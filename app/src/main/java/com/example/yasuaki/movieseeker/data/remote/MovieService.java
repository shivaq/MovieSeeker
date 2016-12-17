package com.example.yasuaki.movieseeker.data.remote;

import com.example.yasuaki.movieseeker.data.model.MovieResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface MovieService {

    @GET("movie/top_rated")
    Observable<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Observable<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);





}
