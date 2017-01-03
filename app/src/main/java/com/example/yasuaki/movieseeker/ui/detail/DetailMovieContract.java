package com.example.yasuaki.movieseeker.ui.detail;


import com.example.yasuaki.movieseeker.data.model.Movie;
import com.example.yasuaki.movieseeker.data.model.Trailer;
import com.example.yasuaki.movieseeker.ui.base.BasePresenter;

import java.util.ArrayList;

public interface DetailMovieContract {

    interface DetailPresenter extends BasePresenter {

        void clearSubscription();
    }

    interface DetailMvpView{
        Movie getMovie();

        void onLoadData(ArrayList<Trailer> trailerResults);

        void showErrorMessage();

        void showProgressBar();

        void hideTrailerProgress();

    }
}
