package com.example.yasuaki.movieseeker.ui.main;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yasuaki.movieseeker.R;
import com.example.yasuaki.movieseeker.data.Movie;
import com.example.yasuaki.movieseeker.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private final String TAG = MovieAdapter.class.getSimpleName();
    private ArrayList<Movie> mTemporaryMovieData;

    private final MovieAdapterOnClickListener mClickListener;

    private Context mContext;

    public MovieAdapter(MovieAdapterOnClickListener clickListener){
        mClickListener = clickListener;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent The ViewGroup that these ViewHolders are contained within.
     * @param viewType You can use this viewType integer to provide a different layout.
     * @return A new MovieAdapterViewHolder that holds the View for each list item
     */
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.main_list_item, parent, false);

        return new MovieAdapterViewHolder(view);
    }

    /**
     * This gets called by the RecyclerView to display the data at the specified position.
     * Download thumbnail image from server into imageView.
     */
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {

        Movie movie = mTemporaryMovieData.get(position);
        String thumbnailPath = movie.getThumbnailPath();

        Uri thumbnailUri = NetworkUtils.buildUrlForThumbnail(thumbnailPath);
        Log.d(TAG, "thumbnailPath is " + thumbnailPath);
        Log.d(TAG, "thumbnailUrl is " + thumbnailUri);
        Picasso.with(mContext).load(thumbnailUri).into(holder.mMovieImageView);
    }

    /**
     * Returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     */
    @Override
    public int getItemCount() {
        if(mTemporaryMovieData == null) return 0;
        return mTemporaryMovieData.size();
    }

    /**
     * Set new data from web on already created MovieAdapter.
     * This method is used to avoid recreating new MovieAdapter.
     */
    public void setMoviewData(ArrayList<Movie> movieData){
        mTemporaryMovieData = movieData;
        notifyDataSetChanged();
    }

    /** MovieAdapterViewHolder **/

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView mMovieImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = (ImageView) itemView.findViewById(R.id.image_movie_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie clickedMovie = mTemporaryMovieData.get(adapterPosition);
            mClickListener.onThumbnailClicked(clickedMovie);
        }
    }

    public interface MovieAdapterOnClickListener{
        void onThumbnailClicked(Movie clickedMovie);
    }
}