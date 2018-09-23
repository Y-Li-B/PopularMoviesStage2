package com.example.android.popularmovies.content;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.example.android.popularmovies.model.Movie;

public class DBUtils {

    //Add a movie to the database.
    public static void addMovie(ContentResolver resolver, Movie movie) {
        //Make content values which represent a row and add proper info to them in each column.
        ContentValues row = new ContentValues();
        row.put(Contract.FavoriteMovies._ID, movie.getId());
        row.put(Contract.FavoriteMovies.ORIGINAL_TITLE_COLUMN, movie.getOriginalTitle());
        row.put(Contract.FavoriteMovies.POSTER_PATH_COLUMN, movie.getPosterPath());
        row.put(Contract.FavoriteMovies.RATING_COLUMN, movie.getRating());
        row.put(Contract.FavoriteMovies.POSTER_THUMBNAIL_URL_COLUMN, movie.getPosterThumbnailURL());
        row.put(Contract.FavoriteMovies.RELEASE_DATE_COLUMN, movie.getReleaseDate());
        row.put(Contract.FavoriteMovies.SYNOPSIS_COLUMN, movie.getSynopsis());

        /*Insert it through the resolver, this will call the insert method we have defined
        in our PopularMoviesContentProviderClass
         */
        resolver.insert(Contract.FavoriteMovies.TABLE_URI, row);
    }

    //Query the database for a specific movie using the movie id.
    public static Cursor queryMovie(ContentResolver resolver, int movieId) {
        //Where we are going to look.
        String[] projection = {Contract.FavoriteMovies._ID};
        //A condition to reduce where we are looking
        String selection = "_id=?";
        //This will replace the ? in the above string, this is supposed to help in avoiding injections.
        String[] selectionArgs = {Integer.toString(movieId)};

        /*Make a URI to perform the query with. It's the table URI and the movie ID appended as a string
        as a path*/
        Uri uri = Uri.withAppendedPath(Contract.FavoriteMovies.TABLE_URI, Integer.toString(movieId));

        /*finally now that we have everything ready, we perform the query through the resolver,
        which will call the insert method we have defined in the PopularMoviesContentProvider*/
        //A cursor is returned with the  movie with the ID that we have requested.
        return resolver.query(uri, projection, selection, selectionArgs, null);
    }

    //This is similar to the above methods, except that it deletes the movie with the given ID.
    public static void removeMovie(ContentResolver resolver, int movieId) {
        resolver.delete(Uri.withAppendedPath(Contract.FavoriteMovies.TABLE_URI, Integer.toString(movieId)),
                "_id=?", new String[]{Integer.toString(movieId)});
    }

    //This returns the an array of Movie objects from a cursor.
    public static Movie[] getMovieList(Cursor c) {
        Movie[] movies = new Movie[c.getCount()];

        for (int i = 0 ; i < c.getCount() ; i++) {
            c.moveToPosition(i);
            Movie movie = new Movie(
                    c.getString(c.getColumnIndex(Contract.FavoriteMovies.ORIGINAL_TITLE_COLUMN)),
                    c.getString(c.getColumnIndex(Contract.FavoriteMovies.POSTER_PATH_COLUMN)),
                    c.getString(c.getColumnIndex(Contract.FavoriteMovies.SYNOPSIS_COLUMN)),
                    c.getInt(c.getColumnIndex(Contract.FavoriteMovies.RATING_COLUMN)),
                    c.getString(c.getColumnIndex(Contract.FavoriteMovies.RELEASE_DATE_COLUMN)),
                    c.getString(c.getColumnIndex(Contract.FavoriteMovies.POSTER_PATH_COLUMN)),
                    c.getInt(c.getColumnIndex(Contract.FavoriteMovies._ID))
            );
            movies[i] = movie;
        }
        return movies;
    }
}