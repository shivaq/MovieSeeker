package com.example.yasuaki.movieseeker.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        implements MovieAdapter.MovieAdapterOnClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener,
        MovieContract.MvpView {

    private final String TAG = MainActivity.class.getSimpleName();

    private final String TOP_RATED = "top_rated";
    private final String MOST_POPULAR = "popular";
    private final String FAVORITE = "favorite";

    @BindView(R.id.recyclerview_main)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_trailer)
    TextView mErrorMessageDisplay;
    @BindView(R.id.progress_loading)
    ProgressBar mProgressBar;

    private MoviePresenter mMoviePresenter;
    private MovieAdapter mMovieAdapter;
    private ArrayList<Movie> mTopRatedMovieList;
    private ArrayList<Movie> mMostPopularMovieList;
    private String mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSortOrder();

        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMoviePresenter = new MoviePresenter(this);

        //If there is no savedInstanceState, load data
        checkSavedInstanceState(savedInstanceState);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(TOP_RATED, mTopRatedMovieList);
        outState.putParcelableArrayList(MOST_POPULAR, mMostPopularMovieList);
        //TODO:Add list for favorite
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "inside onSharedPreferenceChanged");
        if(key.equals(getString(R.string.pref_sort_key))){
            setSortOrder();
            setMovieData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMoviePresenter.clearSubscription();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
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
            case R.id.refresh:
                loadMovies();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /*************************************
     * MVP View methods implementation
     **************************************/

    /**
     * Set movie data to adapter according to preferences
     */
    @Override
    public void setMovieData() {

        ArrayList<Movie> movieList = null;

        //TODO:Add sort for favorite
        //TODO:Pass  movieList data from DB
        switch (mSortOrder) {
            case TOP_RATED:
                movieList = mTopRatedMovieList;
                break;
            case MOST_POPULAR:
                movieList = mMostPopularMovieList;
                break;
            default:
                setSortOrder();
        }

        //Check if Movie data was loaded
        if (movieList == null) {
            loadMovies();
        } else {
            //Movie data is loaded. So set them to adapter
            if (mMovieAdapter == null) {
                mMovieAdapter = new MovieAdapter(this);
            }
            mMovieAdapter.setMovieData(movieList);
            mRecyclerView.setAdapter(mMovieAdapter);
            Log.d(TAG, "inside setMovieData");
        }
    }

    /**
     * Store fetched data to fields. Then set movie data to adapter
     *
     * @param movieList fetched movie data list
     */
    @Override
    public void onLoadData(ArrayList movieList) {

        if (mSortOrder.equals(TOP_RATED)) {
            mTopRatedMovieList = movieList;
        } else {
            mMostPopularMovieList = movieList;
        }
        setMovieData();
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
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgressBar() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Get sort order preference and set it to field
     */
    @Override
    public void setSortOrder(){
        mSortOrder = ActivityUtils.getPreferredSortOrder(this);
    }

    /*****************************
     * MovieAdapter callback
     *****************************/
    //This get called when thumbnail is clicked. Move from here to detailed Activity
    @Override
    public void onThumbnailClicked(Movie clickedMovie) {
        Intent intent = new Intent(this, DetailMovieActivity.class);
        intent.putExtra("clicked_movie", clickedMovie);
        startActivity(intent);
    }

    /*****************************
     * Other methods
     *****************************/

    //Request for movie data depends on sharedPreference
    private void loadMovies() {

        //TODO:get data for favorite
        if (mSortOrder.equals(TOP_RATED)) {
            mMoviePresenter.getTopRatedMovies();
        } else {
            mMoviePresenter.getPopularMovies();
        }
        //TODO:mMovieMvpView.onLoadData(movieList); に、DB のデータを渡す
    }

    /**
     * Check and store savedInstanceState to fields
     */
    private void checkSavedInstanceState(Bundle savedInstanceState) {

        //TODO:Add condition for favorite
        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelableArrayList(TOP_RATED) != null) {
                mTopRatedMovieList = savedInstanceState.getParcelableArrayList(TOP_RATED);
            }
            if (savedInstanceState.getParcelableArrayList(MOST_POPULAR) != null) {
                mMostPopularMovieList = savedInstanceState.getParcelableArrayList(MOST_POPULAR);
            }
            setMovieData();
        } else {
            loadMovies();
        }
    }
}
