package com.example.yasuaki.movieseeker.ui;

import android.content.Context;
import android.util.Log;

import com.example.yasuaki.movieseeker.BuildConfig;
import com.example.yasuaki.movieseeker.data.model.Movie;
import com.example.yasuaki.movieseeker.data.model.MovieResponse;
import com.example.yasuaki.movieseeker.data.remote.MovieService;
import com.example.yasuaki.movieseeker.data.remote.ServiceFactory;
import com.example.yasuaki.movieseeker.util.ActivityUtils;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


class MoviePresenter implements MovieContract.Presenter {

    private static final String TAG = MoviePresenter.class.getSimpleName();
    private final MovieContract.MvpView mMovieMvpView;

    MoviePresenter(MovieContract.MvpView movieMvpView) {
        mMovieMvpView = movieMvpView;
    }

    private MovieService makeMovieService() {
        Log.d(TAG, "inside makeMovieService");
        return ServiceFactory.makeMovieService();
    }

    void getMovies(Context context) {

        Observable observable;
        String sortOrder = ActivityUtils.getPreferredSortOrder(context);

        if (sortOrder.equals("top_rated")) {
            observable = makeMovieService().getTopRatedMovies(BuildConfig.OPEN_MOVIE_DB_API_KEY);
        } else {
            observable = makeMovieService().getPopularMovies(BuildConfig.OPEN_MOVIE_DB_API_KEY);
        }

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Subscriber<MovieResponse>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        Log.d(TAG, "inside onStart");
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
                        Log.d(TAG, "inside onNext");
                        ArrayList<Movie> movieList = movieResponse.getResults();
                        mMovieMvpView.onLoadData(movieList);
                        Log.d(TAG, "after movieAdapter.setMovieData");
                    }
                });
    }
}
