package com.example.yasuaki.movieseeker.ui.detail;


import com.example.yasuaki.movieseeker.data.model.Movie;
import com.example.yasuaki.movieseeker.data.model.Review;
import com.example.yasuaki.movieseeker.data.model.Trailer;
import com.example.yasuaki.movieseeker.ui.base.BasePresenter;

import java.util.ArrayList;

public interface DetailMovieContract {

    interface DetailPresenter extends BasePresenter {

        void clearSubscription();
    }

    interface DetailMvpView{
        Movie getMovie();

        void onLoadTrailer(ArrayList<Trailer> trailerResults);

        void showTrailerErrorMessage();

        void showTrailerProgressBar();

        void hideTrailerProgress();

        void showNoTrailerMessage();

        void onLoadReview(ArrayList<Review> reviewResults);

        void showReviewErrorMessage();

        void showReviewProgressBar();

        void hideReviewProgress();

        void showNoReviewMessage();

        void changeConstraintLayout();
    }
}
