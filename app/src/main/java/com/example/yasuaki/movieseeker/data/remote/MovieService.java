package com.example.yasuaki.movieseeker.data.remote;

import com.example.yasuaki.movieseeker.data.model.MovieResponse;
import com.example.yasuaki.movieseeker.data.model.TrailerResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
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

    @GET("/movie/{id}/videos")
    Observable<TrailerResponse> getMovieTrailer(@Query("api_key") String apiKey,
                                                @Path("id") int id);
}
