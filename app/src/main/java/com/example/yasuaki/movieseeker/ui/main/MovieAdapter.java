package com.example.yasuaki.movieseeker.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yasuaki.movieseeker.R;

/**
 * Created by Yasuaki on 2016/12/10.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private String[] mTemporaryMovieData;

    private final MovieAdapterOnClickListener mClickListener;

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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.main_list_item, parent, false);

        return new MovieAdapterViewHolder(view);
    }

    //TODO:Rewrite javadoc after view turn to be imageview
    /**
     * This gets called by the RecyclerView to display the data at the specified position.
     * In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument
     * that is conveniently passed into us.
     */
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        //TODO: Exchange after view turn to be imageView
        String temporaryUsedText = mTemporaryMovieData[position];
        holder.mMovieTextView.setText(temporaryUsedText);

    }

    /**
     * Returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     */
    @Override
    public int getItemCount() {
        if(mTemporaryMovieData == null) return 0;
        return mTemporaryMovieData.length;
    }

    /**
     * Set new data from web on already created MovieAdapter.
     * This method is used to avoid recreating new MovieAdapter.
     */
    public void setMoviewData(String[] movieData){
        mTemporaryMovieData = movieData;
        notifyDataSetChanged();
    }

    /** MovieAdapterViewHolder **/

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView mMovieTextView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieTextView = (TextView) itemView.findViewById(R.id.text_tobe_imageview_later);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String clickedMovie = mTemporaryMovieData[adapterPosition];
            mClickListener.onThumbnailClicked(clickedMovie);
        }
    }

    public interface MovieAdapterOnClickListener{
        void onThumbnailClicked(String clickedMovie);
    }
}
