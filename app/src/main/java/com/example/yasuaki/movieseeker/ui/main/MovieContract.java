package com.example.yasuaki.movieseeker.ui.main;

import com.example.yasuaki.movieseeker.base.BasePresenter;
import com.example.yasuaki.movieseeker.model.Movie;

import java.util.ArrayList;

/**
 * Created by Yasuaki on 2016/12/17.
 */

public interface MovieContract {

    interface Presenter extends BasePresenter{

    }

    interface MvpView<Presenter>{
        void showMovieDataView();

        void showErrorMessage();

        void openDetail(Movie clickedMovie);

        void onLoadData(ArrayList<Movie> movieList);

    }
}
