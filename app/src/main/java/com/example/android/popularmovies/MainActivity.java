package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;


public class MainActivity extends TaskActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    int lastSpinnerPosition;
    static final String SPINNER_POSITION_KEY = "SPINNER_POSITION";

    Spinner mSpinner;
    TextView mErrorTV;
    RecyclerView mMoviesRV;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        lastSpinnerPosition = savedInstanceState.getInt(SPINNER_POSITION_KEY);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SPINNER_POSITION_KEY, mSpinner.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }


    //Gets a reference to the recycler view object and works on it.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorTV = findViewById(R.id.error_tv);

        mMoviesRV = findViewById(R.id.movie_grid_rv);

        int span = calculateSpanCount(getResources().getDimensionPixelOffset(R.dimen.poster_width));

        mMoviesRV.setLayoutManager(new GridLayoutManager(this, span));
        mMoviesRV.setHasFixedSize(true);


    }

    @Override
    protected TextView getLoadingFailView() {
        return mErrorTV;
    }

    @Override
    protected RecyclerView getLoadingSuccessView() {
        return mMoviesRV;
    }


    //Pass the sort order to the async task and then it will be passed here...
    @Override
    protected Object[] getData(String sortOrder) throws IOException, JSONException {
        return MovieNetworkUtils.getMovieList(sortOrder);
    }

    @Override
    protected void doAdapterMaintenance(Object[] dataSet) {
        MovieListAdapter adapter = (MovieListAdapter) mMoviesRV.getAdapter();
        Movie[] movies = (Movie[]) dataSet;
        //if we have no adapter then create one with the new data set...
        if (adapter == null) {
            mMoviesRV.setAdapter(new MovieListAdapter(movies));
        }
        //else just update the data set and notify that it was changed so it reloads...
        else {
            adapter.setmData(movies);
            adapter.notifyDataSetChanged();
        }
    }

    //inflates the menu, sets up the menus spinner and its adapter...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflates the menu
        getMenuInflater().inflate(R.menu.popularmenu, menu);

        //gets an item from the menu
        MenuItem item = menu.findItem(R.id.sort_by_spinner);

        //casts the action view of the item to a spinner
        mSpinner = (Spinner) item.getActionView();

        /*
        creates an array adapter that works with the string array defined in
        strings.xml and serves its items with a certain layout...
         */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.sort_order,
                R.layout.spinner_item);

        //sets the layout for the drop-down view that's holding the items.
        adapter.setDropDownViewResource(R.layout.spinner_item);

        mSpinner.setGravity(Gravity.CENTER);
        //sets this adapter for the spinner we got from the menu
        mSpinner.setAdapter(adapter);

        mSpinner.setSelection(lastSpinnerPosition);

        //when a certain spinner item is selected take a certain action...
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String order = ((TextView) view).getText().toString();

                //this way of identifying views is not reliable if the app for example
                //gets translated into another language

                if (order.equals(getString(R.string.popularity_sort_order))) {
                    new FetchDataTask(MainActivity.this).execute(MovieNetworkUtils.SORT_ORDER_POPULAR);
                } else if (order.equals(getString(R.string.rating_sort_order))) {
                    new FetchDataTask(MainActivity.this).execute(MovieNetworkUtils.SORT_ORDER_HIGHEST_RATED);
                } else {
                    switchToPreloadState();
                    getSupportLoaderManager().initLoader(0, null, MainActivity.this);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return true;
    }

    /**
     * @param elementWidth  the width of view
     * @return how many views can fit in the screen
     */
    int calculateSpanCount(int elementWidth) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels / elementWidth ;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                Contract.FavoriteMovies.TABLE_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v("VVV", "attempting to reloead but no sure if on correct tab yet");

        //if we are on the correct spinner section
        if (((TextView) mSpinner.getSelectedView()).getText() == getResources().getString(R.string.favorites)) {
            Log.v("VVV", "on correct tab , gonna do maintenance next...");
            doAdapterMaintenance(DBUtils.getMovieList(cursor));
            if (mMoviesRV.getAdapter().getItemCount() == 0) {
                switchToFailState(NO_RESULTS_FOUND_FAILURE);
            } else {
                switchToSuccessState();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //All references to the cursor should be removed here, since
        //it will be closed. I have no references already...
    }
}
