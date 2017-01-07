package com.example.yasuaki.movieseeker.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yasuaki.movieseeker.R;
import com.example.yasuaki.movieseeker.data.model.Movie;
import com.example.yasuaki.movieseeker.data.model.Review;
import com.example.yasuaki.movieseeker.data.model.Trailer;
import com.example.yasuaki.movieseeker.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


//Display detailed movie data
public class DetailMovieActivity extends AppCompatActivity
        implements DetailMovieContract.DetailMvpView,
        TrailerAdapter.TrailerAdapterOnClickListener {

    private final static String TAG = DetailMovieActivity.class.getSimpleName();

    @BindView(R.id.text_detail_title)
    TextView tvTitle;
    @BindView(R.id.image_movie_thumbnail)
    ImageView moviePoster;
    @BindView(R.id.text_release_year)
    TextView tvReleaseDate;
    @BindView(R.id.text_movie_rating)
    TextView tvUserRating;
    @BindView(R.id.text_movie_synopsis)
    TextView tvSynopsis;

    @BindView(R.id.recycler_view_trailer)
    RecyclerView mRecyclerTrailerView;
    @BindView(R.id.tv_error_message_trailer)
    TextView mErrorMessageTrailer;
    @BindView(R.id.progress_trailer)
    ProgressBar mProgressTrailer;
    @BindView(R.id.text_no_trailers)
    TextView mTextViewNoTrailers;

    @BindView(R.id.recycler_view_content)
    RecyclerView mRecyclerReviewView;
    @BindView(R.id.progress_review)
    ProgressBar mProgressBarReview;
    @BindView(R.id.tv_error_message_review)
    TextView mErrorMessageReview;
    @BindView(R.id.text_no_reviews)
    TextView mTextViewNoReviews;

    DetailMoviePresenter mDetailMoviePresenter;
    Movie mMovie;
    TrailerAdapter mTrailerAdapter;
    ReviewAdapter mReviewAdapter;

    ConstraintLayout mConstraintLayout;
    ConstraintSet mNoTrailerConstraintSet = new ConstraintSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);

        mDetailMoviePresenter = new DetailMoviePresenter(this);

        Intent intentFromMain = getIntent();
        mMovie = intentFromMain.getParcelableExtra("clicked_movie");

        tvTitle.setText(mMovie.getMovieTitle());
        Uri thumbnailUri = NetworkUtils.buildUriForThumbnail(mMovie.getThumbnailPath());
        Picasso.with(this).load(thumbnailUri).resize(800, 800).centerInside().into(moviePoster);

        String releaseDate = "Release Date \n" + mMovie.getReleaseDate();
        tvReleaseDate.setText(releaseDate);

        String userRating = "User rating \n" + String.valueOf(mMovie.getRating());
        tvUserRating.setText(userRating + "/10");

        tvSynopsis.setText(mMovie.getMovieOverView());

        //Set up RecyclerView for trailers
        LinearLayoutManager trailerLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerTrailerView.setLayoutManager(trailerLayoutManager);
        mRecyclerTrailerView.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this);
        mRecyclerTrailerView.setAdapter(mTrailerAdapter);
        mDetailMoviePresenter.getMovieTrailer();

        //Prepare ConstraintSet in case there is no trailer.
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.activity_detail);
        mNoTrailerConstraintSet.constrainHeight(R.id.review_label, ConstraintSet.WRAP_CONTENT);
        mNoTrailerConstraintSet.constrainWidth(R.id.review_label, 2000);
        mNoTrailerConstraintSet.connect(R.id.review_label, ConstraintSet.TOP, R.id.text_no_trailers, ConstraintSet.BOTTOM, 0);

        //Set up RecyclerView for reviews
        LinearLayoutManager reviewLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerReviewView.setLayoutManager(reviewLayoutManager);
        mRecyclerReviewView.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter();
        mRecyclerReviewView.setAdapter(mReviewAdapter);
        mDetailMoviePresenter.getReview();

    }

    /**
     * Arrange Constraint layout when there is no trailer
     */
    @Override
    public void changeConstraintLayout() {
        mNoTrailerConstraintSet.applyTo(mConstraintLayout);
    }

    @Override
    public Movie getMovie() {
        return mMovie;
    }

    @Override
    public void onLoadTrailer(ArrayList<Trailer> trailerResults) {
        if (trailerResults == null) {
            mDetailMoviePresenter.getMovieTrailer();
        }

        mTrailerAdapter.setTrailerData(trailerResults);
    }

    @Override
    public void onLoadReview(ArrayList<Review> reviewResults) {
        if (reviewResults == null) {
            mDetailMoviePresenter.getReview();
        }
        mReviewAdapter.setReviewData(reviewResults);
    }


    @Override
    public void showTrailerErrorMessage() {
        mRecyclerTrailerView.setVisibility(View.INVISIBLE);
        mErrorMessageTrailer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTrailerProgressBar() {
        mRecyclerTrailerView.setVisibility(View.INVISIBLE);
        mProgressTrailer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTrailerProgress() {
        mProgressTrailer.setVisibility(View.INVISIBLE);
        mRecyclerTrailerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoTrailerMessage() {
        mRecyclerTrailerView.setVisibility(View.INVISIBLE);
        mTextViewNoTrailers.setVisibility(View.VISIBLE);
    }


    @Override
    public void showReviewErrorMessage() {
        mRecyclerReviewView.setVisibility(View.INVISIBLE);
        mErrorMessageReview.setVisibility(View.VISIBLE);
    }

    @Override
    public void showReviewProgressBar() {
        mRecyclerReviewView.setVisibility(View.INVISIBLE);
        mProgressBarReview.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideReviewProgress() {
        mProgressBarReview.setVisibility(View.INVISIBLE);
        mRecyclerReviewView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoReviewMessage() {
        mRecyclerReviewView.setVisibility(View.INVISIBLE);
        mTextViewNoReviews.setVisibility(View.VISIBLE);
    }

    @Override
    public void onYoutubeClicked(Trailer clickedTrailer) {
        String trailerKey = clickedTrailer.getTrailerKey();

        Uri trailerUri = NetworkUtils.buildUriForTrailer(trailerKey);
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, trailerUri);
        startActivity(youtubeIntent);
    }
}
