package com.example.yasuaki.movieseeker.ui.main;

import com.example.yasuaki.movieseeker.ui.base.BasePresenter;
import com.example.yasuaki.movieseeker.data.model.Movie;

import java.util.ArrayList;

interface MovieContract {

    interface Presenter extends BasePresenter {

        void clearSubscription();
    }

    interface MvpView<Presenter>{

        void onLoadData(ArrayList<Movie> movieList);

        void showFetchedData();

        void showErrorMessage();

        void showProgressBar();

        void hideProgressBar();

    }
}
