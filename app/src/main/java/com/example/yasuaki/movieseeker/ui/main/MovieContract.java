package com.example.yasuaki.movieseeker.ui.main;

import com.example.yasuaki.movieseeker.data.model.Movie;
import com.example.yasuaki.movieseeker.ui.base.BasePresenter;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface MovieContract {

    interface Presenter extends BasePresenter {

        void clearSubscription();
    }

    interface MvpView<Presenter>{

        void onLoadData(List<Movie> movieList);

        void setMovieData();

        void setSortOrder();

        void showFetchedData();

        void showErrorMessage();

        void showProgressBar();

        void hideProgressBar();

    }
}
