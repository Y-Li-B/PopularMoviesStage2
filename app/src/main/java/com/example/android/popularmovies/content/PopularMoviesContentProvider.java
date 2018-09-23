package com.example.android.popularmovies.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class PopularMoviesContentProvider extends ContentProvider {

    //This is the authority of the content provider, which is typically the package name.
    static final String AUTHORITY =  "com.example.android.popularmovies.provider";
    //base URI is scheme + authority
    static final String BASE_URI = "content://" + AUTHORITY;

    DBHelper helper ;
    SQLiteDatabase db ;

    //UriMatcher to facilitate recognizing URIs
    static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    //At class load time, add all the URIs and set their corresponding codes.
    static {
        matcher.addURI(AUTHORITY,Contract.FavoriteMovies.TABLE_NAME, Contract.FavoriteMovies.TABLE_CODE);
        matcher.addURI(AUTHORITY,Contract.FavoriteMovies.TABLE_NAME + "/#", Contract.FavoriteMovies.MOVIE_ID_CODE);
    }

    @Override
    public boolean onCreate() {
        //New instance of DBHelper to deal with database operations.
        helper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        //Get the URI code that corresponds the requested URI through the URI matcher.
        int match = matcher.match(uri);
        Cursor result = null;
        db = helper.getReadableDatabase();

        switch (match){
            /*If the URI was either of querying the favorite movies table in general, or a specific movie
            then perform the query as usual, on the favorite movies table.*/
            case Contract.FavoriteMovies.TABLE_CODE: case Contract.FavoriteMovies.MOVIE_ID_CODE:
               result = db.query(Contract.FavoriteMovies.TABLE_NAME,strings,s,strings1,null,null,s1);
        }

        //Set the notification URI for the cursor, to keep it updated.
        if (result!=null && getContext()!=null) {
            result.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return result;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        //Get the URI code that corresponds the requested URI through the URI matcher.
        int match = matcher.match(uri);
        db = helper.getWritableDatabase();

        switch (match){
            case Contract.FavoriteMovies.TABLE_CODE:
                db.insert(Contract.FavoriteMovies.TABLE_NAME,null,contentValues);
        }

        //If contentValues were not null, then return the URI of the inserted row.
        if(contentValues!=null) {
        /*The line below will return the URI of the table, plus the the ID of the movie
        retrieved as a string through the getAsString() method of contentValues. This results
        in the ID of inserted row.
         */
            return Uri.withAppendedPath(uri, contentValues.getAsString(Contract.FavoriteMovies._ID));
        }
        else return null;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int match = matcher.match(uri);
        db = helper.getWritableDatabase();
        switch(match){
            case Contract.FavoriteMovies.MOVIE_ID_CODE:
                db.delete(Contract.FavoriteMovies.TABLE_NAME,s,strings);
                return 1;
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

