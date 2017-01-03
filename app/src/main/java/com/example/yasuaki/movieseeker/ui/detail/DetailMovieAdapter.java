package com.example.yasuaki.movieseeker.ui.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.yasuaki.movieseeker.data.model.Trailer;

import java.util.ArrayList;

public class DetailMovieAdapter extends RecyclerView.Adapter<DetailMovieAdapter.TrailerReviewViewHolder>{

    private ArrayList<Trailer> mTrailerArrayList;
    private Context mContext;

    DetailMovieAdapter(){

    }

    @Override
    public TrailerReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TrailerReviewViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        //TODO:Trailer か レビューか。
        return super.getItemViewType(position);
    }



    /*****************
     * TrailerReviewViewHolder
     ***************/
    class TrailerReviewViewHolder extends RecyclerView.ViewHolder{

        public TrailerReviewViewHolder(View itemView) {
            super(itemView);
        }
    }
}
