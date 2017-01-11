package com.example.yasuaki.movieseeker.data;

import com.example.yasuaki.movieseeker.data.model.Movie;

import rx.Observable;

public interface MovieDataSource {

    Observable<Movie> getMovie(String movieId);
}
