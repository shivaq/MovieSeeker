package com.example.yasuaki.movieseeker.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.yasuaki.movieseeker.R;
import com.example.yasuaki.movieseeker.util.ActivityUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get reference of Fragment
        MainFragment mainFragment =
                (MainFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.main_fragment_container);

        //If fragment reference is null, Create and inflate new fragment instance
        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    mainFragment,
                    R.id.main_fragment_container);
        }
    }
}
