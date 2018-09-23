package com.example.android.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Review;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewHolder> {

    //todo review this class
    private Review[] mReviewSet;

    public ReviewListAdapter(Review[] reviews){
        this.mReviewSet = reviews;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review,parent,false);
        return new ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review rev = mReviewSet[position];
        holder.mTextView.setText(String.format("%s says:\n\n%s",rev.getAuthor(),rev.getContent()));

    }

    public void setmReviewSet(Review[] reviewSet){
        mReviewSet = reviewSet;

    }



    @Override
    public int getItemCount() {
        return mReviewSet.length;
    }

    class ReviewHolder extends RecyclerView.ViewHolder{

        TextView mTextView;

         ReviewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.review_tv);
        }
    }
}
