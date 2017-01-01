package com.example.yasuaki.movieseeker.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yasuaki.movieseeker.R;
import com.example.yasuaki.movieseeker.data.model.Movie;
import com.example.yasuaki.movieseeker.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


//Display detailed movie data
public class DetailMovieActivity extends AppCompatActivity {

    private final static String TAG = DetailMovieActivity.class.getSimpleName();

    @BindView(R.id.text_detail_title) TextView tvTitle;
    @BindView(R.id.image_movie_thumbnail) ImageView moviePoster;
    @BindView(R.id.text_release_year) TextView tvReleaseDate;
    @BindView(R.id.text_movie_rating) TextView tvUserRating;
    @BindView(R.id.text_movie_synopsis) TextView tvSynopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);

        Intent intentFromMain = getIntent();
        Movie movie = intentFromMain.getParcelableExtra("clicked_movie");

        tvTitle.setText(movie.getMovieTitle());
        Uri thumbnailUri = NetworkUtils.buildUrlForThumbnail(movie.getThumbnailPath());
        Log.d(TAG, "thumbnailPath is " + movie.getThumbnailPath());
        Log.d(TAG, "thumbnailUrl is " + thumbnailUri);
        Picasso.with(this).load(thumbnailUri).resize(800, 800).centerInside().into(moviePoster);

        String releaseDate = "Release Date \n" + movie.getReleaseDate();
        tvReleaseDate.setText(releaseDate);

        String userRating = "User rating \n" + String.valueOf(movie.getRating());
        tvUserRating.setText(userRating + "/10");

        tvSynopsis.setText(movie.getMovieOverView());
    }
}
