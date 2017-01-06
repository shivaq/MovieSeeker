package com.example.yasuaki.movieseeker.ui.detail;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yasuaki.movieseeker.R;
import com.example.yasuaki.movieseeker.data.model.Trailer;
import com.example.yasuaki.movieseeker.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

class DetailMovieAdapter extends RecyclerView.Adapter<DetailMovieAdapter.TrailerViewHolder> {

    private final String TAG = DetailMovieAdapter.class.getSimpleName();

    private final DetailMovieAdapterOnClickListener mYoutubeOnClickListener;
    private Context mContext;
    private ArrayList<Trailer> mTrailerArrayList;

    DetailMovieAdapter(DetailMovieAdapterOnClickListener clickListener) {
        mYoutubeOnClickListener = clickListener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.detail_list_item, parent, false);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {

        Trailer trailer = mTrailerArrayList.get(position);
        ImageView trailerThumbnailView = holder.mTrailerThumbnail;

        Uri trailerThumbnailUri = NetworkUtils.
                buildTrailerThumbnailUri(trailer.getTrailerKey());
        Picasso.with(mContext).load(trailerThumbnailUri)
                .resize(400, 400).centerInside().into(trailerThumbnailView);
    }

    @Override
    public int getItemCount() {
        if (mTrailerArrayList == null) return 0;
        Log.d(TAG, "item size is " + mTrailerArrayList.size());
        return mTrailerArrayList.size();
    }

    void setTrailerData(ArrayList<Trailer> trailerData) {
        mTrailerArrayList = trailerData;
        notifyDataSetChanged();
    }

    /**********************
     * OnClickListener interface
     **********************************/
    interface DetailMovieAdapterOnClickListener {
        void onYoutubeClicked(Trailer clickedTrailer);
    }

    /*****************
     * TrailerViewHolder class
     ***************/
    class TrailerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_trailer)
        ImageView mTrailerThumbnail;
        @BindView(R.id.image_play_icon)
        ImageView mPlayIcon;

        TrailerViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "inside TrailerViewHolder constructor");
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.recycler_detail_item)
        void onItemClicked() {
            int adapterPosition = getAdapterPosition();
            Trailer clickedTrailer = mTrailerArrayList.get(adapterPosition);

            if (mYoutubeOnClickListener != null) {
                mYoutubeOnClickListener.onYoutubeClicked(clickedTrailer);
            }
        }
    }
}
