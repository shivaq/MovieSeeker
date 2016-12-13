package com.example.yasuaki.movieseeker.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yasuaki.movieseeker.R;
import com.example.yasuaki.movieseeker.data.Movie;
import com.example.yasuaki.movieseeker.util.NetworkUtils;
import com.squareup.picasso.Picasso;

//Display detailed movie data
public class DetailMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Intent intentFromMain = getIntent();
        Movie movie = intentFromMain.getParcelableExtra("clicked_movie");

        TextView tvTitle = (TextView) findViewById(R.id.text_detail_title);
        tvTitle.setText(movie.getMovieTitle());

        ImageView moviePoster = (ImageView) findViewById(R.id.image_movie_poster);
        Uri thumbnailUri = NetworkUtils.buildUrlForThumbnail(movie.getThumbnailPath());
        Picasso.with(this).load(thumbnailUri).into(moviePoster);

        TextView tvReleaseDate = (TextView) findViewById(R.id.text_release_year);
        String releaseDate = "Release Date: " + movie.getReleaseDate();
        tvReleaseDate.setText(releaseDate);

        TextView tvUserRating = (TextView) findViewById(R.id.text_movie_rating);
        String userRating = "User rating: " + String.valueOf(movie.getRating());
        tvUserRating.setText(userRating);

        TextView tvSynopsis = (TextView) findViewById(R.id.text_movie_synopsis);
        tvSynopsis.setText(movie.getMovieOverView());

    }
}
