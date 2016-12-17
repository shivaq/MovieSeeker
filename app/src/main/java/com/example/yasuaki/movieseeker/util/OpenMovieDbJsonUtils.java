package com.example.yasuaki.movieseeker.util;

import android.content.Context;
import android.widget.Toast;

import com.example.yasuaki.movieseeker.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Utility functions to handle the Movie DB JSON data.
 */
public final class OpenMovieDbJsonUtils {

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the weather over various days from the forecast.
     *
     * @param context      Callers context
     * @param movieJsonStr JSON response from server
     * @return ArrayList of Movie object
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static ArrayList<Movie> getMovieInfoFromJson(
            Context context, String movieJsonStr) throws JSONException {

        ArrayList<Movie> movieList = new ArrayList<>();

        final String MOVIE_DB_STATUS_CODE = "status_code";
        final String MOVIE_RESULT = "results";
        final String MOVIE_THUMBNAIL = "poster_path";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_TITLE = "original_title";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_RATING = "vote_average";

        JSONObject movieJson = new JSONObject(movieJsonStr);

        if (movieJson.has(MOVIE_DB_STATUS_CODE)) {
            int errorCode = movieJson.getInt(MOVIE_DB_STATUS_CODE);

            Toast.makeText(context, "error! status code is " + errorCode + "." +
                            "check the MovieDb's error code in " +
                            "https://www.themoviedb.org/documentation/api/status-codes"
                    , Toast.LENGTH_LONG).show();
        }

        JSONArray movieArray = movieJson.optJSONArray(MOVIE_RESULT);

        try {
            for (int i = 0; i < movieArray.length(); i++) {

                String thumbnailPath;
                String movieOverView;
                String movieTitle;
                String releaseDate;
                long rating;

                JSONObject movieInfo = movieArray.getJSONObject(i);
                thumbnailPath = movieInfo.getString(MOVIE_THUMBNAIL);
                movieOverView = movieInfo.getString(MOVIE_OVERVIEW);
                movieTitle = movieInfo.getString(MOVIE_TITLE);
                releaseDate = movieInfo.getString(MOVIE_RELEASE_DATE);
                rating = movieInfo.getLong(MOVIE_RATING);

                Movie movie = new Movie(thumbnailPath, movieOverView, movieTitle,
                        releaseDate, rating);

                movieList.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieList;
    }
}
