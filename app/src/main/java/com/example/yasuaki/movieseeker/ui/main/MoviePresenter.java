package com.example.yasuaki.movieseeker.ui.main;

import android.util.Log;

import com.example.yasuaki.movieseeker.BuildConfig;
import com.example.yasuaki.movieseeker.data.model.Movie;
import com.example.yasuaki.movieseeker.data.model.MovieResponse;
import com.example.yasuaki.movieseeker.data.remote.MovieService;
import com.example.yasuaki.movieseeker.data.remote.ServiceFactory;

import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MoviePresenter implements MovieContract.Presenter {

    private static final String TAG = MoviePresenter.class.getSimpleName();
    private final MovieContract.MvpView mMovieMvpView;
    private final ServiceFactory mServiceFactory;

    public MoviePresenter(MovieContract.MvpView movieMvpView, ServiceFactory serviceFactory) {
        mMovieMvpView = movieMvpView;
        mServiceFactory = serviceFactory;
    }

    private MovieService makeMovieService() {
//        return mServiceFactory.makeMovieService();
        Log.d(TAG, "inside makeMovieService");
        return ServiceFactory.makeMovieService();
    }

    //TODO:ModelApiService の クエリメソッドの結果を Presenter クラスにて subscribe（監視）
    public Subscription getMovies() {
        return makeMovieService().getTopRatedMovies(BuildConfig.OPEN_MOVIE_DB_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieResponse>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        mMovieMvpView.showProgressBar();
                    }

                    @Override
                    public void onCompleted() {
                        mMovieMvpView.hideProgressBar();
                        mMovieMvpView.showFetchedData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mMovieMvpView.showErrorMessage();
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(MovieResponse movieResponse) {
                        //TODO:Load 成功時のアクションを記述？
                        Log.d(TAG, "inside onNext");
                        ArrayList<Movie> movieList = movieResponse.getResults();
                        mMovieMvpView.onLoadData(movieList);
                        Log.d(TAG, "after movieAdapter.setMovieData");
                    }
                });
    }
}
