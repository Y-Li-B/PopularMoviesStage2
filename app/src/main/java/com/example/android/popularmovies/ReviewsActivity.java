package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.android.popularmovies.adapters.ReviewListAdapter;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.tasks.FetchDataTask;
import com.example.android.popularmovies.tasks.TaskActivity;
import com.example.android.popularmovies.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;

public class ReviewsActivity extends TaskActivity {
    
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
                .execute(getIntent().getStringExtra(Movie.ID_TAG));
    }

    @Override
    protected TextView getLoadFailureTextView() {
        return mErrorTV;
    }

    @Override
    protected RecyclerView getLoadSuccessView() {
        return mReviewsRV;
    }

    //This is the same string that will be passed to the asynctask
    @Override
    protected Object[] getData(String param) throws IOException, JSONException {
        return NetworkUtils.getReviews(param);
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
