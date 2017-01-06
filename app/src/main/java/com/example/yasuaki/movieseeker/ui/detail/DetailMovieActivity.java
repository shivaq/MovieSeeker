package com.example.yasuaki.movieseeker.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yasuaki.movieseeker.R;
import com.example.yasuaki.movieseeker.data.model.Movie;
import com.example.yasuaki.movieseeker.data.model.Trailer;
import com.example.yasuaki.movieseeker.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


//Display detailed movie data
public class DetailMovieActivity extends AppCompatActivity
        implements DetailMovieContract.DetailMvpView,
        DetailMovieAdapter.DetailMovieAdapterOnClickListener {

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

    DetailMoviePresenter mDetailMoviePresenter;
    Movie mMovie;
    DetailMovieAdapter mDetailMovieAdapter;


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

        //Set up RecyclerView for trailer
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mRecyclerTrailerView.setLayoutManager(layoutManager);
        mRecyclerTrailerView.setHasFixedSize(true);

        mDetailMovieAdapter = new DetailMovieAdapter(this);
        mRecyclerTrailerView.setAdapter(mDetailMovieAdapter);

        //Request trailer data with movie id
        mDetailMoviePresenter.getMovieTrailer();
    }

    @Override
    public Movie getMovie() {
        return mMovie;
    }

    @Override
    public void onLoadData(ArrayList<Trailer> trailerResults) {
        if (trailerResults == null) {
            mDetailMoviePresenter.getMovieTrailer();
        }

        mDetailMovieAdapter.setTrailerData(trailerResults);
    }

    @Override
    public void showErrorMessage() {
        mRecyclerTrailerView.setVisibility(View.INVISIBLE);
        mErrorMessageTrailer.setVisibility(View.VISIBLE);

    }

    @Override
    public void showProgressBar() {
        mRecyclerTrailerView.setVisibility(View.INVISIBLE);
        mProgressTrailer.setVisibility(View.VISIBLE);
        Log.d(TAG, "showProgressBar");
    }

    @Override
    public void hideTrailerProgress() {
        mProgressTrailer.setVisibility(View.INVISIBLE);
        mRecyclerTrailerView.setVisibility(View.VISIBLE);
        Log.d(TAG, "hideTrailerProgress");
    }

    //TODO:load reviews
    private void loadReviews() {

    }

    @Override
    public void onYoutubeClicked(Trailer clickedTrailer) {
        String trailerKey = clickedTrailer.getTrailerKey();

        Uri trailerUri = NetworkUtils.buildUriForTrailer(trailerKey);
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, trailerUri);
        startActivity(youtubeIntent);
    }
}
