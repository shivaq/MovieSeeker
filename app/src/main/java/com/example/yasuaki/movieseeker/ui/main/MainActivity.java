package com.example.yasuaki.movieseeker.ui.main;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasuaki.movieseeker.R;
import com.example.yasuaki.movieseeker.data.Movie;
import com.example.yasuaki.movieseeker.util.NetworkUtils;
import com.example.yasuaki.movieseeker.util.OpenMovieDbJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickListener,
        LoaderManager.LoaderCallbacks<ArrayList<Movie>>{

    private final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;
    private ArrayList<Movie> mMovieList;

    private final int MOVIE_LOADER_ID = 0;

    private String mSortOrder = NetworkUtils.getSortOrder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        //Use StaggeredGridLayout
        // because some poster has different aspect ratio from others.
        StaggeredGridLayoutManager gridLayoutManager
                = new StaggeredGridLayoutManager(2, 1);
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
                if(mMovieData != null){
                    deliverResult(mMovieData);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
                super.onStartLoading();
            }

            @Override
            public ArrayList<Movie> loadInBackground() {

                URL movieDbUrl = NetworkUtils.buildUrl(mSortOrder);

                try{
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieDbUrl);

                    Log.d(TAG, "fetched response is " + jsonMovieResponse);
                    mMovieList = OpenMovieDbJsonUtils.getMovieInfoFromJson(MainActivity.this, jsonMovieResponse);

                    return mMovieList;
                } catch (IOException e){
                    e.printStackTrace();
                    return null;
                } catch (JSONException e){
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

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> movieList) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mMovieAdapter.setMoviewData(movieList);
        if(null == movieList){
            showErrorMessage();
        } else {
            showMovieDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }


    @Override
    public void onThumbnailClicked(Movie clickedMovie) {
        //TODO: (12)intent to detailActivity
        Toast.makeText(this, clickedMovie.getMovieTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId){
            case R.id.sort_popularity:
                mSortOrder = NetworkUtils.getPopularitySort();
                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                return true;
            case R.id.sort_rating:
                mSortOrder = NetworkUtils.getHighRateSort();
                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showMovieDataView(){
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }
}
