package com.example.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

// T type of adapter used in the activity
// S type of dataset used in the adapter

public abstract class TaskActivity extends AppCompatActivity {

    final static int NO_RESULTS_FOUND_FAILURE = 0;
    final static int NORMAL_FALURE = 1;

    //returns the view that will be shown in case of loading failure.
    abstract protected TextView getLoadingFailView();

    //returns the view that will be shown in case of loading of success.
    abstract protected RecyclerView getLoadingSuccessView();

    //hide the view that displays the data , to show the whatever is behind it,
    //it's typically a progress bar...
    protected void switchToPreloadState() {
        getLoadingSuccessView().setVisibility(View.INVISIBLE);
        getLoadingFailView().setVisibility(View.INVISIBLE);
    }

    //This should contain the process you will do in order to get your activity specific
    //data set , the array of objects that's returned should be later cast to the appropriate
    //type in the doAdapterMaintenance method that must be defined in children of this class...
    abstract protected Object[] getData(String param) throws IOException, JSONException;

    //hide the display view , and show whatever you want to show in case of loading failure
    protected void switchToFailState(int failureCode) {
        TextView failView = this.getLoadingFailView();
        if (failureCode == NO_RESULTS_FOUND_FAILURE) failView.setText(R.string.no_results);
        else failView.setText(R.string.fetch_error);
        failView.setVisibility(View.VISIBLE);
        this.getLoadingSuccessView().setVisibility(View.INVISIBLE);
    }

    //show view that displays your data set , and hide the one that indicates failure
    protected void switchToSuccessState() {
        this.getLoadingSuccessView().setVisibility(View.VISIBLE);
        this.getLoadingFailView().setVisibility(View.INVISIBLE);
    }

    //Make a suitable adapter, and load the data into it (You might want to perform a cast on the
    //data set before
    protected abstract void doAdapterMaintenance(Object[] dataSet);
}
