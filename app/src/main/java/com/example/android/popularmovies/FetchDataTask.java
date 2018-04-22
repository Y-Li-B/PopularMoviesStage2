package com.example.android.popularmovies;


import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;

class FetchDataTask extends AsyncTask<String,Void,Object[]> {

    private WeakReference<TaskActivity> callerActivity;

    //Constructor, pass the activity calling this task here...
     FetchDataTask(TaskActivity callerActivity){
    this.callerActivity = new WeakReference<>(callerActivity);
    }

    //Hiding the grid to show the progress bar in the background...
    @Override
    protected void onPreExecute() {
        callerActivity.get().switchToPreloadState();
    }

     /**
     * Builds a url , gets a response from it , then parses the data.
     * @param params An appropriate param that will be passed to the activity so it can fetch
     * it's specific data ....
     * @return String[] | null  should return an array of strings which will be the data source
     * used in the movieAdapter which will then be used for the movieGrid , returns null if
     * loading fails...
     */
    @Override
    protected Object[] doInBackground(String... params) {

        Object[] result = null;

        try {
            result  = callerActivity.get().getData(params[0]);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(JSONException j){
            j.printStackTrace();
        }

        return result;
    }


    /**
     * Checks if loading was ok and takes appropriate action to make an adapter with the
     * new data set and show or hide views.
     *
     * @param result the new data set which the adapter might be supplied with if loading was
     * ok
     */
    @Override
    protected void onPostExecute(Object[] result) {
        //If loading was ok...
        if (callerActivity.get() != null && result != null ) {

            if(result.length != 0) {
                //If there is no adapter set a new one , else update old one.
                callerActivity.get().doAdapterMaintenance(result);

                //finally since loading succeeded, we hide the error text view and show the movie
                //grid
                callerActivity.get().switchToSuccessState();
            }
            else{
                callerActivity.get().switchToFailState(TaskActivity.NO_RESULTS_FOUND_FAILURE);
            }
        }
        //If loading was not ok then show the error text view and hide the movie grid if it's
        //shown
        else{
                callerActivity.get().switchToFailState(TaskActivity.NORMAL_FALURE);
        }

    }
    }
