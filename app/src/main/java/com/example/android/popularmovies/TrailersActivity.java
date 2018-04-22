package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

public class TrailersActivity extends TaskActivity {

    TextView mErrorTV;
    RecyclerView mTrailersRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);

        mErrorTV = findViewById(R.id.error_tv);

        mTrailersRV = findViewById(R.id.trailers_rv);
        mTrailersRV.setLayoutManager(new LinearLayoutManager(this));
        mTrailersRV.setHasFixedSize(true);

        new FetchDataTask(this)
                .execute(getIntent().getStringExtra(Movie.MOVIE_ID_TAG));
    }


    @Override
    protected TextView getLoadingFailView() {
        return mErrorTV;
    }

    @Override
    protected RecyclerView getLoadingSuccessView() {
        return mTrailersRV;
    }

    @Override
    protected Object[] getData(String movieId) throws IOException, JSONException {
        return MovieNetworkUtils.getTrailerKeys(movieId);
    }

    @Override
    protected void doAdapterMaintenance(Object[] dataSet) {
        TrailerListAdapter adapter = (TrailerListAdapter) mTrailersRV.getAdapter();
        String[] keys = (String[])dataSet;
        //if we have no adapter then create one with the new data set...
        if (adapter==null){
            mTrailersRV.setAdapter(new TrailerListAdapter(keys));
        }
        //else just update the data set and notify that it was changed so it reloads...
        else{
            adapter.setmDataSet(keys);
            adapter.notifyDataSetChanged();
        }
    }
}
