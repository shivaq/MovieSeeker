package com.example.yasuaki.movieseeker.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        implements DetailMovieContract.DetailMvpView{

    private final static String TAG = DetailMovieActivity.class.getSimpleName();

    @BindView(R.id.text_detail_title) TextView tvTitle;
    @BindView(R.id.image_movie_thumbnail) ImageView moviePoster;
    @BindView(R.id.text_release_year) TextView tvReleaseDate;
    @BindView(R.id.text_movie_rating) TextView tvUserRating;
    @BindView(R.id.text_movie_synopsis) TextView tvSynopsis;
    @BindView(R.id.recycler_view_trailer)
    RecyclerView mRecyclerTrailer;
    @BindView(R.id.tv_error_message_trailer) TextView mErrorMessageTrailer;
    @BindView(R.id.progress_trailer)
    ProgressBar mProgressTrailer;

    DetailMoviePresenter mDetailMoviePresenter;
    Movie mMovie;


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
        Log.d(TAG, "thumbnailPath is " + mMovie.getThumbnailPath());
        Log.d(TAG, "thumbnailUrl is " + thumbnailUri);
        Picasso.with(this).load(thumbnailUri).resize(800, 800).centerInside().into(moviePoster);

        String releaseDate = "Release Date \n" + mMovie.getReleaseDate();
        tvReleaseDate.setText(releaseDate);

        String userRating = "User rating \n" + String.valueOf(mMovie.getRating());
        tvUserRating.setText(userRating + "/10");

        tvSynopsis.setText(mMovie.getMovieOverView());

        loadTrailers();
    }

    @Override
    public Movie getMovie() {
        return mMovie;
    }

    @Override
    public void onLoadData(ArrayList<Trailer> trailerResults) {

    }

    @Override
    public void showErrorMessage() {
        mRecyclerTrailer.setVisibility(View.INVISIBLE);
        mErrorMessageTrailer.setVisibility(View.VISIBLE);

    }

    @Override
    public void showProgressBar() {
        mRecyclerTrailer.setVisibility(View.INVISIBLE);
        mProgressTrailer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTrailerProgress() {
        mProgressTrailer.setVisibility(View.INVISIBLE);
        mRecyclerTrailer.setVisibility(View.VISIBLE);
    }

    private void loadTrailers(){
        mDetailMoviePresenter.getMovieTrailer();
    }

    private void setTrailer(){
        //TODO:Adapter を new して Trailer を渡す
    }

    //TODO:load reviews
    private void loadReviews(){

    }
}
