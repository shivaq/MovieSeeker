package com.example.yasuaki.movieseeker.ui.main;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.example.yasuaki.movieseeker.data.Movie;
import com.example.yasuaki.movieseeker.util.ActivityUtils;
import com.example.yasuaki.movieseeker.util.NetworkUtils;
import com.example.yasuaki.movieseeker.util.OpenMovieDbJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

//Entry point of Movie Seeker app
public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickListener,
        LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;
    private ArrayList<Movie> mMovieList;

    private final int MOVIE_LOADER_ID = 0;

    private String mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSortOrder = ActivityUtils.getPreferredSortOrder(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

//        //Use StaggeredGridLayout
//        // because some poster has different aspect ratio from others.
//        StaggeredGridLayoutManager gridLayoutManager
//                = new StaggeredGridLayoutManager(2, 1);
//        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_loading);

        int loaderId = MOVIE_LOADER_ID;
        LoaderManager.LoaderCallbacks<ArrayList<Movie>> callback = MainActivity.this;
        Bundle bundleForLoader = null;
        getLoaderManager().initLoader(loaderId, bundleForLoader, callback);
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
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<ArrayList<Movie>>(this) {

            ArrayList<Movie> mMovieData = null;

            /**
             * This is called when LoaderManager calls startLoading()
             * to start an asynchronous load of the Loader's data.
             */
            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
                super.onStartLoading();
            }

            /**
             * This is the method of the AsyncTaskLoader that will load and parse the JSON data
             * from The Movie DB in the background.
             *
             * @return Movie data from The Movie DB as an ArrayList of Movie object.
             *         null if an error occurs
             */
            @Override
            public ArrayList<Movie> loadInBackground() {

                URL movieDbUrl = NetworkUtils.buildUrl(mSortOrder);

                try {
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieDbUrl);

                    Log.d(TAG, "fetched response is " + jsonMovieResponse);
                    mMovieList = OpenMovieDbJsonUtils.getMovieInfoFromJson(MainActivity.this, jsonMovieResponse);

                    return mMovieList;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            /**
             * Sends the result of the load to the registered listener.
             *
             * @param data The result of the load
             */
            @Override
            public void deliverResult(ArrayList<Movie> data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader    The Loader that has finished.
     * @param movieList The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> movieList) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mMovieAdapter.setMoviewData(movieList);
        if (null == movieList) {
            showErrorMessage();
        } else {
            showMovieDataView();
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        //When we use CursorLoader, we may implement here.
    }

    //This get called when thumbnail is clickd. Move here to detailed Activity
    @Override
    public void onThumbnailClicked(Movie clickedMovie) {
        Intent intent = new Intent(this, DetailMovieActivity.class);
        intent.putExtra("clicked_movie", clickedMovie);
        startActivity(intent);
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

    /**
     * This method will make the View for the movie thumbnail visible and
     * hide the error message.
     */
    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the View for the error message visible and
     * movie thumbnail hide the .
     */
    private void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    //Restart loading from the Movie DB
    void updateMovie() {
        getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    //Get called if sort order preference is changed
    void onSortOrderChanged() {
        updateMovie();
    }
}
