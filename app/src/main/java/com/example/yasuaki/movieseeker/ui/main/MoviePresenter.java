package com.example.yasuaki.movieseeker.ui.main;

import android.content.Context;
import android.util.Log;

import com.example.yasuaki.movieseeker.data.MovieRepository;
import com.example.yasuaki.movieseeker.data.model.Movie;
import com.example.yasuaki.movieseeker.util.NetworkUtils;

import java.util.List;

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
    private final MovieRepository mMovieRepository;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;

    MoviePresenter(Context context, MovieContract.MvpView movieMvpView) {
        mContext = context;
        mMovieMvpView = movieMvpView;
        mMovieRepository = MovieRepository.getInstance(mContext);
        mCompositeSubscription = new CompositeSubscription();
    }

    /**
     * Unsubscribe CompositeSubscription and its reusable
     */
    @Override
    public void clearSubscription() {
        mCompositeSubscription.clear();
    }

    /**
     * Request web server for top rated movie data and watch the process.
     * Operate completion or error of the process.
     */
    void getTopRatedMovies() {

        mMovieMvpView.showProgressBar();

        mCompositeSubscription.add(mMovieRepository.getTopMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Movie>>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        if(NetworkUtils.isOnline(mContext)){
                            mMovieMvpView.showErrorMessage();
                        } else {
                            mMovieMvpView.showNetworkError();
                        }
                    }

                    /**
                     * Pass fetched data to MainActivity
                     * @param movieList fetched Movie data list
                     */
                    @Override
                    public void onNext(List<Movie> movieList) {
                        mMovieMvpView.onLoadData(movieList);
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
        mCompositeSubscription.add(mMovieRepository.getPopularMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Movie>>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        if(NetworkUtils.isOnline(mContext)){
                            mMovieMvpView.showErrorMessage();
                        } else {
                            mMovieMvpView.showNetworkError();
                        }
                    }

                    @Override
                    public void onNext(List<Movie> movieList) {
                        mMovieMvpView.onLoadData(movieList);
                        mMovieMvpView.hideProgressBar();
                        mMovieMvpView.showFetchedData();
                    }
                }));
    }

    /**
     * Request Sql database for favorite movie data and watch the process.
     * Operate completion or error of the process.
     */
    void getFavoriteMovies(){
        mMovieMvpView.showFetchedData();
        mMovieMvpView.showProgressBar();
        mCompositeSubscription.add(mMovieRepository.getFavoriteMovie()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Movie>>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        if(NetworkUtils.isOnline(mContext)){
                            mMovieMvpView.showErrorMessage();
                        } else {
                            mMovieMvpView.showNetworkError();
                        }
                    }

                    @Override
                    public void onNext(List<Movie> movieList) {
                        mMovieMvpView.onLoadData(movieList);
                        mMovieMvpView.hideProgressBar();
                        mMovieMvpView.showFetchedData();
                    }
                }));
    }
}
