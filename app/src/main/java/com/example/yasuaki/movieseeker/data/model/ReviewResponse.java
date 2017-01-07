package com.example.yasuaki.movieseeker.data.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewResponse {

    @SerializedName("results")
    private ArrayList<Review> reviewResults = new ArrayList<>();

    public ArrayList<Review> getReviewResults() {
        return reviewResults;
    }
}
