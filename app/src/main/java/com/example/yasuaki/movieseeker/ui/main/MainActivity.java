package com.example.yasuaki.movieseeker.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.yasuaki.movieseeker.R;
import com.example.yasuaki.movieseeker.util.ActivityUtils;

import butterknife.ButterKnife;

/**
 * Entry point of Movie Seeker app.
 * Responsible for UI display mechanism as a MVP patterns View
 */
public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

//    @BindView(R.id.toolbar)
//    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MainFragment mainFragment =
                (MainFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);

        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    mainFragment,
                    R.id.fragment_container);
        }
    }
}
