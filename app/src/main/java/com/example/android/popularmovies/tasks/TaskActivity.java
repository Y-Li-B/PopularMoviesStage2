package com.example.android.popularmovies.tasks;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public abstract class TaskActivity extends AppCompatActivity {


    //returns the the text that contains an message which will be shown on load failure.
    protected abstract  TextView getLoadFailureTextView();

    //returns the view that will be shown in case of loading of success.
    protected abstract View getLoadSuccessView();

    /*hide both of the load failure and success view show the whatever is behind it,
    it's typically a progress bar...*/
    protected void switchToLoadState() {
        getLoadSuccessView().setVisibility(View.INVISIBLE);
        getLoadFailureTextView().setVisibility(View.INVISIBLE);
    }

    /*This should contain the process you will do in order to get your activity specific
    data set*/
    abstract protected Object[] getData(String param) throws Exception;


    //When doesn't finish successfully, or there are no results.
    protected void switchToFailState(String message) {
        TextView failView = this.getLoadFailureTextView();
        failView.setText(message);
        failView.setVisibility(View.VISIBLE);
        this.getLoadSuccessView().setVisibility(View.INVISIBLE);
    }

    //show view that displays your data set , and hide the one that indicates failure
    protected void switchToSuccessState() {
        this.getLoadSuccessView().setVisibility(View.VISIBLE);
        this.getLoadFailureTextView().setVisibility(View.INVISIBLE);
    }


    protected abstract void doAdapterMaintenance(Object[] dataSet);

}
