package com.example.yasuaki.movieseeker.ui.main;

import android.util.Log;

import com.example.yasuaki.movieseeker.BuildConfig;
import com.example.yasuaki.movieseeker.data.model.Movie;
import com.example.yasuaki.movieseeker.data.model.MovieResponse;
import com.example.yasuaki.movieseeker.data.remote.MovieService;
import com.example.yasuaki.movieseeker.data.remote.ServiceFactory;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * As a MVP patterns Presenter, retrieve data and updates the UI
 */
class MoviePresenter implements MovieContract.Presenter {

    private static final String TAG = MoviePresenter.class.getSimpleName();
    private final MovieContract.MvpView mMovieMvpView;
    private CompositeSubscription mCompositeSubscription;

    MoviePresenter(MovieContract.MvpView movieMvpView) {
        mMovieMvpView = movieMvpView;
        mCompositeSubscription = new CompositeSubscription();
    }

    /**
     * Unsubscribe CompositeSubscription and its reusable
     */
    @Override
    public void clearSubscription() {
        mCompositeSubscription.clear();
    }

    private MovieService makeMovieService() {
        return ServiceFactory.makeMovieService();
    }

    /**
     * Request web server for top rated movie data and watch the process.
     * Operate completion or error of the process.
     */
    void getTopRatedMovies() {

        mMovieMvpView.showProgressBar();

        mCompositeSubscription.add(makeMovieService()
                .getTopRatedMovies(BuildConfig.OPEN_MOVIE_DB_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieResponse>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mMovieMvpView.showErrorMessage();
                        Log.e(TAG, e.toString());
                    }

                    /**
                     * Pass fetched data to MainActivity
                     * @param movieResponse fetched Movie data list
                     */
                    @Override
                    public void onNext(MovieResponse movieResponse) {
                        Log.d(TAG, "inside onNext");
                        ArrayList<Movie> movieList = movieResponse.getResults();
                        mMovieMvpView.onLoadData(movieList);
                        Log.d(TAG, "after movieAdapter.setMovieData");
                        mMovieMvpView.hideProgressBar();
                        mMovieMvpView.showFetchedData();
                    }
                }));
    }

    /**
     * Request web server for most popular movie data and watch the process.
     * Operate completion or error of the process.
     */
    void getPopularMovies() {
        mMovieMvpView.showProgressBar();
        mCompositeSubscription.add(makeMovieService()
                .getPopularMovies(BuildConfig.OPEN_MOVIE_DB_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieResponse>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mMovieMvpView.showErrorMessage();
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(MovieResponse movieResponse) {
                        Log.d(TAG, "inside onNext");
                        ArrayList<Movie> movieList = movieResponse.getResults();
                        mMovieMvpView.onLoadData(movieList);
                        Log.d(TAG, "after movieAdapter.setMovieData");
                        mMovieMvpView.hideProgressBar();
                        mMovieMvpView.showFetchedData();
                    }
                }));
    }

}
