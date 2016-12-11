package com.example.yasuaki.movieseeker.ui.main;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yasuaki.movieseeker.R;
import com.example.yasuaki.movieseeker.util.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickListener,
        LoaderManager.LoaderCallbacks<String[]>{

    private final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;

    private final int MOVIE_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false );
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_loading);

        int loaderId = MOVIE_LOADER_ID;
        LoaderManager.LoaderCallbacks<String[]> callback = MainActivity.this;
        Bundle bundleForLoader = null;
        getLoaderManager().initLoader(loaderId, bundleForLoader, callback);
    }

    @Override
    public Loader<String[]> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<String[]>(this) {

            String[] mMovieData = null;

            /**
             * This is called when LoaderManager calls startLoading()
             * to start an axynchronous load of the Loader's data.
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
            public String[] loadInBackground() {

                URL movieDbUrl = NetworkUtils.buildUrl();

                try{
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieDbUrl);

                    //TODO: make this fit to GridView imageView
                    String[] fakeJsonData = new String[2];
                    fakeJsonData[0] = jsonMovieResponse;
                    fakeJsonData[1] = jsonMovieResponse;

                    return fakeJsonData;

                } catch (IOException e){
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
            public void deliverResult(String[] data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] strings) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mMovieAdapter.setMoviewData(strings);
        if(null == strings){
            showErrorMessage();
        } else {
            showMovieDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }


    @Override
    public void onThumbnailClicked(String clickedMovie) {
        //TODO: intent to detailActivity
    }

    //TODO: Get sort order from sort boolean and sort

    private void showMovieDataView(){
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    /**  FetchMovieTask  **/

    public class FetchMovieTask extends AsyncTask<String, Void, String[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... strings) {
            return new String[0];
        }
    }
}
