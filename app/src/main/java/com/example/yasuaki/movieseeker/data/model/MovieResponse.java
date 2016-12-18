package com.example.yasuaki.movieseeker.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

//Data model to fetch and hold List of Movies
public class MovieResponse {

    @SerializedName("results")
    private ArrayList<Movie> results = new ArrayList<>();

    public ArrayList<Movie> getResults() {
        return results;
    }
}
