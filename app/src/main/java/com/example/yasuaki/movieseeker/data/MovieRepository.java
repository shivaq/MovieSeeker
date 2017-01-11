package com.example.yasuaki.movieseeker.data;


import android.content.Context;

import com.example.yasuaki.movieseeker.BuildConfig;
import com.example.yasuaki.movieseeker.data.local.MovieLocalDataSource;
import com.example.yasuaki.movieseeker.data.model.Movie;
import com.example.yasuaki.movieseeker.data.model.MovieResponse;
import com.example.yasuaki.movieseeker.data.remote.MovieRemoteDataSource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class MovieRepository implements MovieDataSource {

    private static MovieRepository INSTANCE = null;

    private final MovieLocalDataSource mMovieLocalDataSource;

    private Map<String, Movie> mCachedMovies;

    private MovieRepository(Context context) {
        mMovieLocalDataSource = MovieLocalDataSource.getInstance(context);
    }

    public static MovieRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MovieRepository(context);
        }
        return INSTANCE;
    }

    private MovieRemoteDataSource makeMovieService() {
        return MovieRemoteDataSource.ServiceFactory.makeMovieService();
    }

    public Observable<List<Movie>> getTopMovies(){

        return makeMovieService()
                .getTopRatedMovies(BuildConfig.OPEN_MOVIE_DB_API_KEY)
                //Get MovieResponse and return List<Movie>
                .map(new Func1<MovieResponse, List<Movie>>() {
                    @Override
                    public List<Movie> call(MovieResponse movieResponse) {
                        List<Movie> remoteMovieData = movieResponse.getResults();
                        return remoteMovieData;
                    }
                });
    }

    public Observable<List<Movie>> getPopularMovies(){

        return makeMovieService()
                .getPopularMovies(BuildConfig.OPEN_MOVIE_DB_API_KEY)
                .map(new Func1<MovieResponse, List<Movie>>() {
                    @Override
                    public List<Movie> call(MovieResponse movieResponse) {
                        List<Movie> remoteMovieData = movieResponse.getResults();
                        return remoteMovieData;
                    }
                });
    }

    public Observable<Movie> getMovie(final String movieId) {

        final Movie cachedMovie = getMovieWithId(movieId);

        if (cachedMovie != null) {
            return Observable.just(cachedMovie);
        }

        if (mCachedMovies == null) {
            mCachedMovies = new LinkedHashMap<>();
        }

        Observable<Movie> localMovie = getMovieWithIdFromLocal(movieId);

        Observable<Movie> remoteMovie = mMovieLocalDataSource.getMovie(movieId)
                .doOnNext(new Action1<Movie>() {
                    @Override
                    public void call(Movie movie) {
                        mMovieLocalDataSource.insertMovie(movie);
                        mCachedMovies.put(Integer.toString(movie.getMovieId()), movie);
                    }
                });

        return Observable.concat(localMovie, remoteMovie).first()
                .map(new Func1<Movie, Movie>() {
                    @Override
                    public Movie call(Movie movie) {
                        if (movie == null) {
                            throw new NoSuchElementException("No task found with taskId " + movieId);
                        }
                        return movie;
                    }
                });
    }

    private Movie getMovieWithId(String movieId) {
        if (mCachedMovies == null || mCachedMovies.isEmpty()) {
            return null;
        } else {
            return mCachedMovies.get(movieId);
        }
    }

    //
    Observable<Movie> getMovieWithIdFromLocal(final String movieId) {
        return mMovieLocalDataSource.getMovie(movieId)
                //movie -> mCachedMovies.put(movieId, movie)
                .doOnNext(new Action1<Movie>() {

                    @Override
                    public void call(Movie movie) {
                        mCachedMovies.put(movieId, movie);
                    }
                })
                .first();
    }

}
