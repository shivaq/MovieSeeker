package com.example.yasuaki.movieseeker.ui.main;

import android.content.Context;
import android.util.Log;

import com.example.yasuaki.movieseeker.data.MovieRepository;
import com.example.yasuaki.movieseeker.data.model.Movie;

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

    MoviePresenter(MovieContract.MvpView movieMvpView, Context context) {
        mMovieMvpView = movieMvpView;
        mMovieRepository = MovieRepository.getInstance(context);
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
                        mMovieMvpView.showErrorMessage();
                        Log.e(TAG, e.toString());
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
                        mMovieMvpView.showErrorMessage();
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(List<Movie> movieList) {
                        mMovieMvpView.onLoadData(movieList);
                        mMovieMvpView.hideProgressBar();
                        mMovieMvpView.showFetchedData();
                    }
                }));
    }

//    void getFavoriteMovies(){
//        mMovieMvpView.showProgressBar();
//        mCompositeSubscription.add(mMovieRepository.getFavoriteMovie()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Cursor>() {
//
//
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mMovieMvpView.showErrorMessage();
//                        Log.e(TAG, e.toString());
//                    }
//
//                    @Override
//                    public void onNext(Cursor cursor) {
//
//                        try{
//                            if(!cursor.moveToNext()){
//                                //TODO:show no favorite message
//                            }
//                            cursor.
//                        } finally{
//                            cursor.close();
//                        }
//                    }
//                }));
//    }
}
