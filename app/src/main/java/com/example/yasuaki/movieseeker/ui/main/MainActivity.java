package com.example.yasuaki.movieseeker.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yasuaki.movieseeker.R;
import com.example.yasuaki.movieseeker.data.model.Movie;
import com.example.yasuaki.movieseeker.ui.detail.DetailMovieActivity;
import com.example.yasuaki.movieseeker.ui.preference.SettingsActivity;
import com.example.yasuaki.movieseeker.util.ActivityUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Entry point of Movie Seeker app.
 * Responsible for UI display mechanism as a MVP patterns View
 */
public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickListener, MovieContract.MvpView {

    private final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.recyclerview_main)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay;
    @BindView(R.id.progress_loading)
    ProgressBar mProgressBar;

    private MoviePresenter mMoviePresenter;
    private String mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSortOrder = ActivityUtils.getPreferredSortOrder(this);

        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMoviePresenter = new MoviePresenter(this);
        loadMovies();
    }

    //Check if preference is changed. If it is changed fetch data from the Movie DB
    @Override
    protected void onResume() {
        super.onResume();

        String prefSort = ActivityUtils.getPreferredSortOrder(this);
        if (prefSort != null && !mSortOrder.equals(prefSort)) {
            onSortOrderChanged();
        }
        mSortOrder = prefSort;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMoviePresenter.clearSubscription();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.pref_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /********   MVP View methods implementation   ***********/
    /**
     * Set fetched data on movieAdapter. Then set the adapter on recyclerView
     * @param movieList fetched movie data list
     */
    @Override
    public void onLoadData(ArrayList movieList) {
        MovieAdapter movieAdapter = new MovieAdapter(this);
        movieAdapter.setMovieData(movieList);
        mRecyclerView.setAdapter(movieAdapter);
    }

    /**
     * This method will make the View for the movie thumbnail visible and
     * hide the error message.
     */
    @Override
    public void showFetchedData() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the View for the error message visible and
     * movie thumbnail hide the .
     */
    @Override
    public void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /*********** MovieAdapter callback ******************/
    //This get called when thumbnail is clicked. Move from here to detailed Activity
    @Override
    public void onThumbnailClicked(Movie clickedMovie) {
        Intent intent = new Intent(this, DetailMovieActivity.class);
        intent.putExtra("clicked_movie", clickedMovie);
        startActivity(intent);
    }

    //Request for movie data depends on sharedPreference
    private void loadMovies(){
        String sortOrder = ActivityUtils.getPreferredSortOrder(this);
        if (sortOrder.equals("top_rated")) {
            mMoviePresenter.getTopRatedMovies();
        } else {
            mMoviePresenter.getPopularMovies();
        }
    }

    //Get called if sort order preference is changed
    private void onSortOrderChanged() {
        loadMovies();
    }
}
