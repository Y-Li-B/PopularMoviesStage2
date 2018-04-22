package com.example.android.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class DBUtils {

    static void addMovie(ContentResolver resolver, Movie movie) {

        ContentValues row = new ContentValues();
        row.put(Contract.FavoriteMovies._ID, movie.getId());
        row.put(Contract.FavoriteMovies.ORIGINAL_TITLE_COLULMN, movie.getOriginalTitle());
        row.put(Contract.FavoriteMovies.POSTER_PATH_COLUMN, movie.getPosterPath());
        row.put(Contract.FavoriteMovies.RATING_COLUMN, movie.getRating());
        row.put(Contract.FavoriteMovies.POSTER_THUMBNAIL_URL_COLUMN, movie.getPosterThumbnailURL());
        row.put(Contract.FavoriteMovies.RELEASE_DATE_COLUMN, movie.getReleaseDate());
        row.put(Contract.FavoriteMovies.SYNOPOSIS_COLUMN, movie.getSynopsis());
        resolver.insert(Contract.FavoriteMovies.TABLE_URI, row);
    }

    static Cursor queryMovie(ContentResolver resolver, int movieId) {
        String[] projection = {Contract.FavoriteMovies._ID};
        String selection = "_id=?";
        String[] selectionArgs = {Integer.toString(movieId)};
        Uri uri = Uri.withAppendedPath(Contract.FavoriteMovies.TABLE_URI, Integer.toString(movieId));

        return resolver.query(uri, projection, selection, selectionArgs, null);
    }

    static void removeMovie(ContentResolver resolver, int movieId) {
        resolver.delete(Uri.withAppendedPath(Contract.FavoriteMovies.TABLE_URI, Integer.toString(movieId)),
                "_id=?", new String[]{Integer.toString(movieId)});
    }

    static Movie[] getMovieList(Cursor c) {
        Movie[] movies = new Movie[c.getCount()];

        for (int i = 0 ; i < c.getCount() ; i++) {
            c.moveToPosition(i);
            Movie movie = new Movie(
                    c.getString(c.getColumnIndex(Contract.FavoriteMovies.ORIGINAL_TITLE_COLULMN)),
                    c.getString(c.getColumnIndex(Contract.FavoriteMovies.POSTER_PATH_COLUMN)),
                    c.getString(c.getColumnIndex(Contract.FavoriteMovies.SYNOPOSIS_COLUMN)),
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