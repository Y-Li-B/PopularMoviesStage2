package com.example.android.popularmovies;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.popularmovies.adapters.MovieListAdapter;
import com.example.android.popularmovies.content.Contract;
import com.example.android.popularmovies.content.DBUtils;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.tasks.FetchDataTask;
import com.example.android.popularmovies.tasks.TaskActivity;
import com.example.android.popularmovies.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;


public class MainActivity extends TaskActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    static final String SPINNER_POSITION_KEY = "SPINNER_POSITION";

    TextView mErrorMessageTV;

    RecyclerView mMoviesRV;

    Spinner mSpinner;

    int lastSpinnerPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessageTV = findViewById(R.id.error_tv);
        mMoviesRV = findViewById(R.id.movie_grid_rv);

        //Calculate how many movie posters can fit horizontally.
        int span = calculateSpanCount(getResources().getDimensionPixelOffset(R.dimen.poster_width));

        //Set layout manager with the calculated span.
        mMoviesRV.setLayoutManager(new GridLayoutManager(this, span));
        mMoviesRV.setHasFixedSize(true);

        //Remember the lastSpinnerPosition.
        if (savedInstanceState!=null) {
            lastSpinnerPosition = savedInstanceState.getInt(SPINNER_POSITION_KEY);
        }
    }

    /**
     * @param elementWidth  the width of view
     * @return how many views can fit in the screen
     */
    private int calculateSpanCount(int elementWidth) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels / elementWidth ;
    }

    private void initSortOrderSpinner(Spinner spinner){
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

        spinner.setGravity(Gravity.CENTER);
        //sets this adapter for the spinner we got from the menu
        spinner.setAdapter(adapter);


        spinner.setSelection(lastSpinnerPosition);

        //when a certain spinner item is selected take a certain action...
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String requestedOrder = ((TextView) view).getText().toString();

                /*This should work fine even if the app is translated. Because
                R.String.<String Name> should return the language specific string automatically,
                and fallback to default is none is found.*/
                if (requestedOrder.equals(getString(R.string.popularity_sort_order))) {
                    new FetchDataTask(MainActivity.this).execute(NetworkUtils.SORT_ORDER_POPULAR);
                } else if (requestedOrder.equals(getString(R.string.rating_sort_order))) {
                    new FetchDataTask(MainActivity.this).execute(NetworkUtils.SORT_ORDER_HIGHEST_RATED);
                } else {
                    switchToLoadState();
                    getSupportLoaderManager().initLoader(0, null, MainActivity.this);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    //inflates the menu, sets up the menus spinner and its adapter...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflates the menu
        getMenuInflater().inflate(R.menu.menu, menu);

        //Find the spinner in the menu as an item.
        MenuItem item = menu.findItem(R.id.sort_by_spinner);

        //cast the action view of the item to a spinner.
        mSpinner = (Spinner) item.getActionView();

        initSortOrderSpinner(mSpinner);

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SPINNER_POSITION_KEY, mSpinner.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }


    @Override
    protected TextView getLoadFailureTextView() {
        return mErrorMessageTV;
    }

    @Override
    protected View getLoadSuccessView() {
        return mMoviesRV;
    }

    //Pass the sortOrder to the async task and then it will be passed here.
    @Override
    public Movie[] getData(String sortOrder) throws IOException, JSONException {
        return NetworkUtils.getMovieList(sortOrder);
    }



    //Creates a new adapter with the data, if there is none. Else just updates the old adapter.
    @Override
    public void doAdapterMaintenance(Object[] dataSet) {
        MovieListAdapter adapter = (MovieListAdapter) mMoviesRV.getAdapter();
        Movie[] movies = (Movie[]) dataSet;
        //if we have no adapter then create one with the new data set...
        if (adapter == null) {
            mMoviesRV.setAdapter(new MovieListAdapter(movies));
        }
        //else just update the data set and notify that it was changed so it reloads...
        else {
            adapter.setData(movies);
            adapter.notifyDataSetChanged();
        }
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
        //if we are still on the correct spinner section
        if (((TextView) mSpinner.getSelectedView()).getText() == getResources().getString(R.string.favorites)) {
            //Update the adapter with new data.
            doAdapterMaintenance(DBUtils.getMovieList(cursor));
            //If nothing was found...
            if (mMoviesRV.getAdapter().getItemCount() == 0) {
                switchToFailState(getString(R.string.no_results));
            } else {
                switchToSuccessState();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
            /*hide both of the load failure and success view show the whatever is behind it,
    it's typically a progress bar...*/
