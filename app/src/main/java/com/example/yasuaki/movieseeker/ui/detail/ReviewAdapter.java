package com.example.yasuaki.movieseeker.ui.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yasuaki.movieseeker.R;
import com.example.yasuaki.movieseeker.data.model.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private ArrayList<Review> mReviewArrayList;

    ReviewAdapter() {
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = mReviewArrayList.get(position);

        TextView reviewAuthorView = holder.mReviewAuthor;
        TextView reviewContentView = holder.mReview;

        String reviewAuthor = "Reviewer: " + review.getAuthor();
        reviewAuthorView.setText(reviewAuthor);
        String reviewContent = review.getContent();
        reviewContentView.setText(reviewContent);
    }

    @Override
    public int getItemCount() {
        if (mReviewArrayList == null) return 0;
        return mReviewArrayList.size();
    }

    void setReviewData(ArrayList<Review> reviewData) {
        mReviewArrayList = reviewData;
        notifyDataSetChanged();
    }

    /*******
     * ReviewViewHolder
     *******/

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_review_author)
        TextView mReviewAuthor;
        @BindView(R.id.text_review)
        TextView mReview;

        ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
