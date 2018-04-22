package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

public class ReviewsActivity extends TaskActivity  {
    
    TextView mErrorTV; 
    RecyclerView mReviewsRV;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        
        mErrorTV = findViewById(R.id.error_tv);
        
        mReviewsRV = findViewById(R.id.reviews_rv);

        mReviewsRV = findViewById(R.id.reviews_rv);
        mReviewsRV.setLayoutManager(new LinearLayoutManager(this));
        mReviewsRV.setHasFixedSize(true);

        new FetchDataTask(this)
                .execute(getIntent().getStringExtra(Movie.MOVIE_ID_TAG));
    }

    @Override
    protected TextView getLoadingFailView() {
        return mErrorTV;
    }

    @Override
    protected RecyclerView getLoadingSuccessView() {
        return mReviewsRV;
    }

    //This is the same string that will be passed to the asynctask
    @Override
    protected Object[] getData(String param) throws IOException, JSONException {
        return MovieNetworkUtils.getReviews(param);
    }

    @Override
    protected void doAdapterMaintenance(Object[] dataSet) {
        ReviewListAdapter adapter = (ReviewListAdapter) mReviewsRV.getAdapter();
        Review[] reviews = (Review[])dataSet;
        //if we have no adapter then create one with the new data set...
        if (adapter==null){
            mReviewsRV.setAdapter(new ReviewListAdapter(reviews));
        }
        //else just update the data set and notify that it was changed so it reloads...
        else{
            adapter.setmReviewSet(reviews);
            adapter.notifyDataSetChanged();
        }

    }
}
