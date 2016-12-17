package com.example.yasuaki.movieseeker.ui.main;

import com.example.yasuaki.movieseeker.base.BasePresenter;
import com.example.yasuaki.movieseeker.data.model.Movie;

import java.util.ArrayList;

public interface MovieContract {

    interface Presenter extends BasePresenter{

    }

    interface MvpView<Presenter>{
        void showFetchedData();

        void showErrorMessage();

        void onLoadData(ArrayList<Movie> movieList);

        void showProgressBar();

        void hideProgressBar();

    }
}
