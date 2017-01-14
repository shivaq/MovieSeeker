package com.example.yasuaki.movieseeker.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TrailerResponse {

    @SerializedName("results")
    private ArrayList<Trailer> trailerResults = new ArrayList<>();

    public ArrayList<Trailer> getResults() {
        return trailerResults;
    }
}
