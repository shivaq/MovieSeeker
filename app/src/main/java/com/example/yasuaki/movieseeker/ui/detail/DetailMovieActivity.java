package com.example.yasuaki.movieseeker.ui.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.yasuaki.movieseeker.R;
import com.example.yasuaki.movieseeker.util.ActivityUtils;

import butterknife.ButterKnife;


//Display detailed movie data
public class DetailMovieActivity extends AppCompatActivity {

    private final static String TAG = DetailMovieActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        DetailMovieFragment detailMovieFragment =
                (DetailMovieFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.detail_fragment_container);

        if (detailMovieFragment == null) {
            detailMovieFragment = DetailMovieFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    detailMovieFragment,
                    R.id.detail_fragment_container);
        }
    }
}
