package com.example.yasuaki.movieseeker.data;


import android.content.Context;
import android.database.Cursor;

import com.example.yasuaki.movieseeker.BuildConfig;
import com.example.yasuaki.movieseeker.data.local.MovieLocalDataSource;
import com.example.yasuaki.movieseeker.data.local.MoviePersistenceContract;
import com.example.yasuaki.movieseeker.data.model.Movie;
import com.example.yasuaki.movieseeker.data.model.MovieResponse;
import com.example.yasuaki.movieseeker.data.remote.MovieRemoteDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

public class MovieRepository {

    private static final String TAG = MovieRepository.class.getSimpleName();

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

    public void insertMovie(Movie movie) {
        mMovieLocalDataSource.insertMovie(movie);
    }

    public void deleteMovie(String movieId) {
        mMovieLocalDataSource.deleteMovie(movieId);
    }

    public Observable<List<Movie>> getTopMovies() {

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

    public Observable<List<Movie>> getPopularMovies() {

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

    public Observable<List<Movie>> getFavoriteMovie() {

        return mMovieLocalDataSource.getFavoriteMovie()
                .map(new Func1<Cursor, List<Movie>>() {

                    @Override
                    public List<Movie> call(Cursor cursor) {

                        List<Movie> movieList = new ArrayList<>();

                        try {
                            if (cursor.moveToFirst()) {
                                for (int i = 0; i < cursor.getCount(); i++) {
                                    int movieId = cursor.getInt(MoviePersistenceContract.INDEX_MOVIE_ID);
                                    String title = cursor.getString(MoviePersistenceContract.INDEX_TITLE);
                                    String thumbnailPath = cursor.getString(MoviePersistenceContract.INDEX_POSTER_PATH);
                                    String overView = cursor.getString(MoviePersistenceContract.INDEX_OVERVIEW);
                                    String releaseDate = cursor.getString(MoviePersistenceContract.INDEX_RELEASE_DATE);
                                    float voteAverage = cursor.getFloat(MoviePersistenceContract.INDEX_VOTE_AVERAGE);
                                    boolean isFavorite = cursor.getInt(MoviePersistenceContract.INDEX_FAVORITE) != 1;
                                    Movie movie = new Movie(movieId, title,
                                            thumbnailPath, overView, releaseDate,
                                            voteAverage, isFavorite);
                                    movieList.add(movie);
                                    cursor.moveToNext();
                                }
                            }
                        } finally {
                            cursor.close();
                        }
                        return movieList;
                    }
                });
    }

    public Observable<Cursor> getMovieFromLocalWithId(String movieId) {
        return mMovieLocalDataSource.getMovieWithId(movieId);
    }


}
