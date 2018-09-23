package com.example.android.popularmovies.tasks;


import android.os.AsyncTask;

import com.example.android.popularmovies.R;

import java.lang.ref.WeakReference;

public class FetchDataTask extends AsyncTask<String,Void,Object[]> {

    private WeakReference<TaskActivity> callerActivity;

    //Constructor, pass the activity calling this task here...
     public FetchDataTask(TaskActivity callerActivity){
    this.callerActivity = new WeakReference<>(callerActivity);
    }

    //Hiding the grid to show the progress bar in the background...
    @Override
    protected void onPreExecute() {
        callerActivity.get().switchToLoadState();
    }


    @Override
    protected Object[] doInBackground(String... params) {

        Object[] result = null;
        try {
            result  = callerActivity.get().getData(params[0]);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }


    @Override
    protected void onPostExecute(Object[] result) {

         //Strong reference from weak but this should be ok since we are in local context.
         TaskActivity activity = callerActivity.get();

        if (activity != null) {

            if (result != null) {

                if (result.length != 0) {
                    //If there is no adapter set a new one , else update old one.
                    activity.doAdapterMaintenance(result);
                    //finally since loading succeeded, we switch to success state.
                    activity.switchToSuccessState();
                } else {
                    activity.switchToFailState(activity.getString(R.string.no_results));
                }

            }

            //If loading was not ok then show the error text view and hide the movie grid if it's
            //shown
            else activity.switchToFailState(activity.getString(R.string.fetch_error));

        }
    }


    }
