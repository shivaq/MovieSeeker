package com.example.yasuaki.movieseeker.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

//Data model to fetch and hold List of Movies
public class MovieResponse {

    @SerializedName("results")
    private List<Movie> results = new ArrayList<>();

    public List<Movie> getResults() {
        return results;
    }
}
