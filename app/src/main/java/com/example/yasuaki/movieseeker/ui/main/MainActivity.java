package com.example.yasuaki.movieseeker.ui.main;

import android.content.Context;
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
import java.util.List;

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

    static final String EXTRA_CLICKED_MOVIE = "com.example.yasuaki.movieseeker.EXTRA_CLICKED_MOVIE";

    private final String TOP_RATED = "top_rated";
    private final String MOST_POPULAR = "popular";
    private final String FAVORITE = "favorite";

    @BindView(R.id.recyclerview_main)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_trailer)
    TextView mErrorMessageDisplay;
    @BindView(R.id.progress_loading)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_network_error_message)
    TextView mNetworkErrorMessageDisplay;

    private MoviePresenter mMoviePresenter;
    private MovieAdapter mMovieAdapter;
    private ArrayList<Movie> mTopRatedMovieList;
    private ArrayList<Movie> mMostPopularMovieList;
    private ArrayList<Movie> mFavoriteMovieList;
    private String mSortOrder;

    public static Intent getStartIntent(Context context, Movie clickedMovie) {
        Intent intent = new Intent(context, DetailMovieActivity.class);
        intent.putExtra(EXTRA_CLICKED_MOVIE, clickedMovie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMoviePresenter = new MoviePresenter(this, this);

        checkSortOrder();

        //If there is no savedInstanceState, load data
        getMovieData(savedInstanceState);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        mMoviePresenter.clearSubscription();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(TOP_RATED, mTopRatedMovieList);
        outState.putParcelableArrayList(MOST_POPULAR, mMostPopularMovieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sort_key))) {
            checkSortOrder();
        }
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
     * Store fetched data to fields. Then set movie data to adapter
     *
     * @param movieList fetched movie data list
     */
    @Override
    public void onLoadData(List movieList) {

        Log.d(TAG, "onLoadData: mSortOrder is " + mSortOrder);
        switch (mSortOrder) {
            case TOP_RATED:
                mTopRatedMovieList = (ArrayList) movieList;
                break;
            case MOST_POPULAR:
                mMostPopularMovieList = (ArrayList) movieList;
                break;
            case FAVORITE:
                mFavoriteMovieList = (ArrayList) movieList;
                break;
        }
        setMovieData();
    }

    /**
     * Set movie data to adapter according to preferences
     */
    @Override
    public void setMovieData() {

        ArrayList<Movie> movieList = null;
        switch (mSortOrder) {
            case TOP_RATED:
                movieList = mTopRatedMovieList;
                break;
            case MOST_POPULAR:
                movieList = mMostPopularMovieList;
                break;
            case FAVORITE:
                movieList = mFavoriteMovieList;
            default:
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
        }
    }

    /**
     * Get sort order preference and set it to field
     */
    @Override
    public void checkSortOrder() {
        Log.d(TAG, "checkSortOrder: sort order is now " + mSortOrder);
        if (mSortOrder == null || !mSortOrder.equals(ActivityUtils.getPreferredSortOrder(this))) {
            mSortOrder = ActivityUtils.getPreferredSortOrder(this);
            loadMovies();
        }
    }

    /**
     * This method will make the View for the movie thumbnail visible and
     * hide the error message.
     */
    @Override
    public void showFetchedData() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mNetworkErrorMessageDisplay.setVisibility(View.INVISIBLE);
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
    public void showNetworkError() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mNetworkErrorMessageDisplay.setVisibility(View.VISIBLE);
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


    /*****************************
     * MovieAdapter callback
     *****************************/
    //This get called when thumbnail is clicked. Move to DetailMovieActivity
    @Override
    public void onThumbnailClicked(Movie clickedMovie) {
        startActivity(getStartIntent(this, clickedMovie));
    }

    /*****************************
     * private methods
     *****************************/


    /**
     * Check and store savedInstanceState to fields
     */
    private void getMovieData(Bundle savedInstanceState) {

        Log.d(TAG, "getMovieData: ");
        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelableArrayList(TOP_RATED) != null) {
                mTopRatedMovieList = savedInstanceState.getParcelableArrayList(TOP_RATED);
            }
            if (savedInstanceState.getParcelableArrayList(MOST_POPULAR) != null) {
                mMostPopularMovieList = savedInstanceState.getParcelableArrayList(MOST_POPULAR);
            }
            if (mSortOrder.equals(FAVORITE)) {
                mMoviePresenter.getFavoriteMovies();
            }
            setMovieData();
        } else {
            loadMovies();
        }
    }

    //Request for movie data depends on sharedPreference
    private void loadMovies() {

        Log.d(TAG, "loadMovies: sortOrder is " + mSortOrder);
        switch (mSortOrder) {
            case TOP_RATED:
                mMoviePresenter.getTopRatedMovies();
                break;
            case MOST_POPULAR:
                mMoviePresenter.getPopularMovies();
                break;
            case FAVORITE:
                mMoviePresenter.getFavoriteMovies();
                break;
            default:
                Log.e(TAG, "loadMovies: Illegal sort order");
        }
    }
}
