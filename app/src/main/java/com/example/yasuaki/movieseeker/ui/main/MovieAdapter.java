package com.example.yasuaki.movieseeker.ui.main;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yasuaki.movieseeker.R;
import com.example.yasuaki.movieseeker.data.Movie;
import com.example.yasuaki.movieseeker.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private final String TAG = MovieAdapter.class.getSimpleName();
    private ArrayList<Movie> mMovieArrayList;

    private final MovieAdapterOnClickListener mClickListener;

    private Context mContext;

    /**
     * Creates a MovieAdapter.
     *
     * @param clickListener The on-click handler for this adapter. This single handler is called
     *                      when an item is clicked.
     */
    MovieAdapter(MovieAdapterOnClickListener clickListener) {
        mClickListener = clickListener;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent   The ViewGroup that these ViewHolders are contained within.
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

        TextView movieTitle = holder.mMovieTitleText;
        ImageView posterImage = holder.mMovieImageView;
        Movie movie = mMovieArrayList.get(position);
        String thumbnailPath = movie.getThumbnailPath();

        if (thumbnailPath.equals("null")) {
            movieTitle.setVisibility(View.VISIBLE);
            posterImage.setVisibility(View.INVISIBLE);
            movieTitle.setText(movie.getMovieTitle());

        } else {
            movieTitle.setVisibility(View.INVISIBLE);
            posterImage.setVisibility(View.VISIBLE);
            Uri thumbnailUri = NetworkUtils.buildUrlForThumbnail(thumbnailPath);
            Log.d(TAG, "thumbnailPath is " + thumbnailPath);
            Log.d(TAG, "thumbnailUrl is " + thumbnailUri);
            Picasso.with(mContext).load(thumbnailUri).into(posterImage);
        }
    }

    /**
     * Returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     */
    @Override
    public int getItemCount() {
        if (mMovieArrayList == null) return 0;
        return mMovieArrayList.size();
    }

    /**
     * Set new data from web on already created MovieAdapter.
     * This method is used to avoid recreating new MovieAdapter.
     */
    void setMoviewData(ArrayList<Movie> movieData) {
        mMovieArrayList = movieData;
        notifyDataSetChanged();
    }

    /************************** MovieAdapterViewHolder ****************************/

    /**
     * Cache of the children views for a main list item.
     */
    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image_movie_thumbnail_listitem) ImageView mMovieImageView;
        @BindView(R.id.text_null_poster) TextView mMovieTitleText;

        MovieAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param view The View that was clicked
         */
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie clickedMovie = mMovieArrayList.get(adapterPosition);
            mClickListener.onThumbnailClicked(clickedMovie);
        }
    }

    /**
     * The interface that receives onClick messages.
     */
    interface MovieAdapterOnClickListener {
        void onThumbnailClicked(Movie clickedMovie);
    }
}
