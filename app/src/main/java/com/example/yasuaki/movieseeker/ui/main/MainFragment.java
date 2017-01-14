package com.example.yasuaki.movieseeker.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class MainFragment extends Fragment implements MovieAdapter.MovieAdapterOnClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener,
        MovieContract.MvpView{

    static final String EXTRA_CLICKED_MOVIE = "com.example.yasuaki.movieseeker.EXTRA_CLICKED_MOVIE";
    private static final String LIST_STATE_KEY = "com.example.yasuaki.movieseeker.LIST_STATE";

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
    private RecyclerView.LayoutManager mLayoutManager;
    private Parcelable mListState;

    public static Intent getStartIntent(Context context, Movie clickedMovie) {
        Intent intent = new Intent(context, DetailMovieActivity.class);
        intent.putExtra(EXTRA_CLICKED_MOVIE, clickedMovie);
        return intent;
    }

    public MainFragment() {
    }

    public static MainFragment newInstance(){
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, rootView);

        mLayoutManager =
                new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMoviePresenter = new MoviePresenter(getActivity(), this);

        checkSortOrderAndLoad();

        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save list item state
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);

        outState.putParcelableArrayList(TOP_RATED, mTopRatedMovieList);
        outState.putParcelableArrayList(MOST_POPULAR, mMostPopularMovieList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Retrieve list item state
        if(mListState != null){
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListState != null) {
            //Restore list item state
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    /**
     * Clear subscription.
     * Unregister OnSharedPreferenceListener
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMoviePresenter.clearSubscription();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.pref_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.refresh:
                loadMovies();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sort_key))) {
            checkSortOrderAndLoad();
        }
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
    public void checkSortOrderAndLoad() {
        if (mSortOrder == null || !mSortOrder.equals(ActivityUtils.getPreferredSortOrder(getActivity()))) {
            mSortOrder = ActivityUtils.getPreferredSortOrder(getActivity());
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
        startActivity(getStartIntent(getActivity(), clickedMovie));
    }

    /*****************************
     * private methods
     *****************************/

    //Request for movie data depends on sharedPreference
    private void loadMovies() {

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
        }
    }
}
