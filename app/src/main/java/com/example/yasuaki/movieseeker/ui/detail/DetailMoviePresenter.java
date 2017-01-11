package com.example.yasuaki.movieseeker.ui.detail;

import android.content.Context;
import android.util.Log;

import com.example.yasuaki.movieseeker.BuildConfig;
import com.example.yasuaki.movieseeker.data.MovieRepository;
import com.example.yasuaki.movieseeker.data.local.MovieDbHelper;
import com.example.yasuaki.movieseeker.data.local.MovieLocalDataSource;
import com.example.yasuaki.movieseeker.data.model.Movie;
import com.example.yasuaki.movieseeker.data.model.Review;
import com.example.yasuaki.movieseeker.data.model.ReviewResponse;
import com.example.yasuaki.movieseeker.data.model.Trailer;
import com.example.yasuaki.movieseeker.data.model.TrailerResponse;
import com.example.yasuaki.movieseeker.data.remote.MovieRemoteDataSource;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class DetailMoviePresenter implements DetailMovieContract.DetailPresenter {

    private static final String TAG = DetailMoviePresenter.class.getSimpleName();
    private DetailMovieContract.DetailMvpView mDetailedMovieMvpView;
    private CompositeSubscription mCompositeSubscription;

    private MovieLocalDataSource mMovieLocalDataSource;
    private MovieRepository mMovieRepository;
    private MovieDbHelper mOpenHelper;

    DetailMoviePresenter(DetailMovieContract.DetailMvpView detailedMovieMvpView, Context context) {
        mDetailedMovieMvpView = detailedMovieMvpView;
        mCompositeSubscription = new CompositeSubscription();
        mMovieLocalDataSource = MovieLocalDataSource.getInstance(context);
        mMovieRepository = MovieRepository.getInstance(context);
        mOpenHelper = new MovieDbHelper(context);
    }


    /**
     * Unsubscribe CompositeSubscription and its reusable
     */
    @Override
    public void clearSubscription() {
        mCompositeSubscription.clear();
    }

    private MovieRemoteDataSource makeMovieService() {
        return MovieRemoteDataSource.ServiceFactory.makeMovieService();
    }

    void getMovieTrailer() {

        mDetailedMovieMvpView.showTrailerProgressBar();
        Log.d(TAG, "Movie Id is " + mDetailedMovieMvpView.getMovie().getMovieId());

        mCompositeSubscription.add(makeMovieService()
                        .getMovieTrailer(mDetailedMovieMvpView.getMovie().getMovieId(),
                        BuildConfig.OPEN_MOVIE_DB_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TrailerResponse>() {

                               @Override
                               public void onCompleted() {
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mDetailedMovieMvpView.showTrailerErrorMessage();
                               }

                               @Override
                               public void onNext(TrailerResponse trailerResponse) {
                                   ArrayList<Trailer> trailerList = trailerResponse.getResults();
                                   Log.d(TAG, "TrailerList is " + trailerList.toString());
                                   if (trailerList.size() == 0) {
                                       mDetailedMovieMvpView.showNoTrailerMessage();
                                       mDetailedMovieMvpView.changeConstraintLayout();
                                   } else {
                                       mDetailedMovieMvpView.onLoadTrailer(trailerList);
                                       mDetailedMovieMvpView.hideTrailerProgress();
                                   }
                               }
                           }
                ));
    }

    void getReview() {
        mDetailedMovieMvpView.showReviewProgressBar();

        mCompositeSubscription.add(makeMovieService()
                .getReview(mDetailedMovieMvpView.getMovie().getMovieId(),
                        BuildConfig.OPEN_MOVIE_DB_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ReviewResponse>() {

                               @Override
                               public void onCompleted() {
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mDetailedMovieMvpView.showReviewErrorMessage();
                               }

                               @Override
                               public void onNext(ReviewResponse reviewResponse) {
                                   ArrayList<Review> reviewList = reviewResponse.getReviewResults();
                                   if (reviewList.size() == 0) {
                                       mDetailedMovieMvpView.showNoReviewMessage();
                                   }
                                   mDetailedMovieMvpView.onLoadReview(reviewList);
                                   mDetailedMovieMvpView.hideReviewProgress();
                               }
                           }
                ));
    }

    //TODO:Get Movie movieToContentValues DB
    public void queryMovie(String movieId) {

        mCompositeSubscription.add(mMovieRepository.getMovie(movieId)


                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Movie>() {

                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {

                               }

                               @Override
                               public void onNext(Movie movie) {

                               }
                           }
                ));


    }

    public void saveMovie(Movie movie) {
        Log.d(TAG, "inside insertMovie");
        mMovieLocalDataSource.insertMovie(movie);
    }

    public void deleteMovie(String movieId) {
        mMovieLocalDataSource.deleteMovie(movieId);
    }


}
