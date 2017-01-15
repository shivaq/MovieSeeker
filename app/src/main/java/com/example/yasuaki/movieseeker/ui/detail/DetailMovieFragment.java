package com.example.yasuaki.movieseeker.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasuaki.movieseeker.R;
import com.example.yasuaki.movieseeker.data.model.Movie;
import com.example.yasuaki.movieseeker.data.model.Review;
import com.example.yasuaki.movieseeker.data.model.Trailer;
import com.example.yasuaki.movieseeker.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DetailMovieFragment extends Fragment implements DetailMovieContract.DetailMvpView,
        TrailerAdapter.TrailerAdapterOnClickListener {

    private static final String TAG = DetailMovieFragment.class.getSimpleName();
    static final String EXTRA_CLICKED_MOVIE = "com.example.yasuaki.movieseeker.EXTRA_CLICKED_MOVIE";
    private static final String LIST_STATE_KEY = "com.example.yasuaki.movieseeker.LIST_STATE_KEY";
    private static final String TRAILER_LIST_KEY = "com.example.yasuaki.movieseeker.TRAILER_LIST_KEY";
    private static final String REVIEW_LIST_KEY = "com.example.yasuaki.movieseeker.REVIEW_LIST_KEY";

    @BindView(R.id.image_movie_backdrop)
    ImageView movieBackdrop;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
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
    @BindView(R.id.button_favorite)
    ImageView mFavoriteButton;


    private DetailMoviePresenter mDetailMoviePresenter;
    private Movie mMovie;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private LinearLayoutManager mReviewLayoutManager;
    private Parcelable mListState;
    private ConstraintLayout mConstraintLayout;
    private ConstraintSet mNoTrailerConstraintSet = new ConstraintSet();
    private Toast mToast;

    boolean mIsFavorite;
    private ShareActionProvider mShareActionProvider;
    private ArrayList<Trailer> mTrailerList;
    private ArrayList<Review> mReviewList;

    public static DetailMovieFragment newInstance() {
        return new DetailMovieFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        mDetailMoviePresenter = new DetailMoviePresenter(getActivity(), this);

        Intent intentFromMain = getActivity().getIntent();
        mMovie = intentFromMain.getParcelableExtra(EXTRA_CLICKED_MOVIE);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)
                rootView.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(mMovie.getMovieTitle());
        toolbar.setTitle(mMovie.getMovieTitle());

        //set backdrop image
        Uri backdropUri = NetworkUtils.buildUriForThumbnail(mMovie.getBackdropPath());
        Picasso.with(getActivity()).load(backdropUri).into(movieBackdrop);

        //set thumbnail image
        Uri thumbnailUri = NetworkUtils.buildUriForThumbnail(mMovie.getThumbnailPath());
        Picasso.with(getActivity()).load(thumbnailUri)
                .resize(800, 800)
                .centerInside()
                .into(moviePoster);

        String releaseDate = "Release Date \n" + mMovie.getReleaseDate();
        tvReleaseDate.setText(releaseDate);

        String userRating = "User rating \n" + String.valueOf(mMovie.getVoteAverage() + "/10");
        tvUserRating.setText(userRating);
        tvSynopsis.setText(mMovie.getMovieOverView());

        //Set up RecyclerView for trailers
        LinearLayoutManager trailerLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerTrailerView.setLayoutManager(trailerLayoutManager);
        mRecyclerTrailerView.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(this);
        mRecyclerTrailerView.setAdapter(mTrailerAdapter);

        //Set up RecyclerView for reviews
        mReviewLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerReviewView.setLayoutManager(mReviewLayoutManager);
        mRecyclerReviewView.setHasFixedSize(true);

        mReviewAdapter = new ReviewAdapter();
        mRecyclerReviewView.setAdapter(mReviewAdapter);

        //Prepare ConstraintSet in case there is no trailer.
        mConstraintLayout = (ConstraintLayout) rootView.findViewById(R.id.activity_detail);
        mNoTrailerConstraintSet.constrainHeight(R.id.review_label, ConstraintSet.WRAP_CONTENT);
        mNoTrailerConstraintSet.constrainWidth(R.id.review_label, 2000);
        mNoTrailerConstraintSet.connect(R.id.review_label, ConstraintSet.TOP, R.id.text_no_trailers, ConstraintSet.BOTTOM, 0);

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: " + savedInstanceState);
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            Log.d(TAG, "onActivityCreated: mListState is " + mListState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save list item state
        mListState = mReviewLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
        outState.putParcelableArrayList(TRAILER_LIST_KEY, mTrailerList);
        outState.putParcelableArrayList(REVIEW_LIST_KEY, mReviewList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDetailMoviePresenter.clearSubscription();
    }

    @Override
    public void onResume() {
        super.onResume();
        mDetailMoviePresenter.getMovieTrailer();
        mDetailMoviePresenter.getReview();
        //query db and sync favorite state
        mDetailMoviePresenter.getMovieWithId(Integer.toString(mMovie.getMovieId()));

        if (mListState != null) {
            mReviewLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_menu, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = new ShareActionProvider(getContext());
        MenuItemCompat.setActionProvider(item, mShareActionProvider);
    }

    private void createShareTrailerIntent() {

        if (mTrailerList.size() > 0) {
            String trailerKey = mTrailerList.get(0).getTrailerKey();
            Uri trailerUri = NetworkUtils.buildUriForTrailer(trailerKey);

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, mMovie.getMovieTitle() + " Trailer\n" + trailerUri);

            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(sendIntent);
            } else {
                Log.d(TAG, "createShareTrailerIntent: Share Action Provider is null");
            }
        }
    }


    /**
     * Arrange Constraint layout when there is no trailer
     */
    @Override
    public void changeConstraintLayout() {
        mNoTrailerConstraintSet.applyTo(mConstraintLayout);
    }

    /**
     * Sync favorite state of DetailMovieActivity, Movie object, image color, and db.
     */
    @Override
    public void syncFavorite(boolean isFavorite) {
        mIsFavorite = isFavorite;
        mMovie.setFavorite(isFavorite);
        if (isFavorite) {
            mFavoriteButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.starColor));
        } else {
            mFavoriteButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.grayColor));
        }
    }

    @Override
    public Movie getMovie() {
        return mMovie;
    }


    @Override
    public void onLoadTrailer(ArrayList<Trailer> trailerList) {
        mTrailerList = trailerList;
        if (trailerList == null) {
            mDetailMoviePresenter.getMovieTrailer();
        }
        mTrailerAdapter.setTrailerData(mTrailerList);
        createShareTrailerIntent();
    }

    @Override
    public void onLoadReview(ArrayList<Review> reviewList) {
        mReviewList = reviewList;
        if (reviewList == null) {
            mDetailMoviePresenter.getReview();
        }
        mReviewAdapter.setReviewData(mReviewList);
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

    /***************
     * implementation for TrailerAdapterOnClickListener
     */
    @Override
    public void onYoutubeClicked(Trailer clickedTrailer) {
        String trailerKey = clickedTrailer.getTrailerKey();

        Uri trailerUri = NetworkUtils.buildUriForTrailer(trailerKey);
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, trailerUri);
        startActivity(youtubeIntent);
    }

    /***********
     * set onClick
     ************/
    @OnClick(R.id.button_favorite)
    void onItemClicked() {

        if (mIsFavorite) {//If it's favorite
            syncFavorite(false);//Change favorite to not favorite.

            //delete from db
            mDetailMoviePresenter.deleteMovie(Integer.toString(mMovie.getMovieId()));

            if (mToast != null) {
                mToast.cancel();
            }
            //Tell it's out of your favorite
            mToast = Toast.makeText(getActivity(), mMovie.getMovieTitle() + "\nis not your favorite anymore.", Toast.LENGTH_SHORT);
            mToast.show();

        } else {//If it's not favorite
            syncFavorite(true);

            //insert to db
            mDetailMoviePresenter.insertMovie(mMovie);

            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getActivity(), mMovie.getMovieTitle() + "\nis added to your favorite.", Toast.LENGTH_SHORT);
            mToast.show();
        }
    }
}
