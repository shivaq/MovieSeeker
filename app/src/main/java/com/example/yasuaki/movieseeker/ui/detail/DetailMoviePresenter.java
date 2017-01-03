package com.example.yasuaki.movieseeker.ui.detail;

import com.example.yasuaki.movieseeker.BuildConfig;
import com.example.yasuaki.movieseeker.data.model.Trailer;
import com.example.yasuaki.movieseeker.data.model.TrailerResponse;
import com.example.yasuaki.movieseeker.data.remote.MovieService;
import com.example.yasuaki.movieseeker.data.remote.ServiceFactory;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class DetailMoviePresenter implements DetailMovieContract.DetailPresenter{

    private static final String TAG = DetailMoviePresenter.class.getSimpleName();
    private DetailMovieContract.DetailMvpView mDetailedMovieMvpView;
    private CompositeSubscription mCompositeSubscription;

    DetailMoviePresenter(DetailMovieContract.DetailMvpView detailedMovieMvpView){
        mDetailedMovieMvpView = detailedMovieMvpView;
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

    void getMovieTrailer(){

        mDetailedMovieMvpView.showProgressBar();

        mCompositeSubscription.add(makeMovieService()
                .getMovieTrailer(BuildConfig.OPEN_MOVIE_DB_API_KEY,
                        mDetailedMovieMvpView.getMovie().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TrailerResponse>(){

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mDetailedMovieMvpView.showErrorMessage();
                    }

                    @Override
                    public void onNext(TrailerResponse trailerResponse) {
                        ArrayList<Trailer> trailerList = trailerResponse.getResults();
                        //TODO：Activity にて Adapter を new
                        //TODO: Adapter にて各 アイテムに、URL を紐付けていく
                        mDetailedMovieMvpView.hideTrailerProgress();
                    }
                })
        );
    }
}
