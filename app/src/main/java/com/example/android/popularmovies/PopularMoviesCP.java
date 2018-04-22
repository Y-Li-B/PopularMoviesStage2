package com.example.android.popularmovies;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class PopularMoviesCP extends ContentProvider {

    DBHelper helper ;
    SQLiteDatabase db ;

    static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        matcher.addURI(Contract.AUTHORITY,Contract.FavoriteMovies.TABLE_NAME, Contract.FavoriteMovies.TABLE_CODE);
        matcher.addURI(Contract.AUTHORITY,Contract.FavoriteMovies.TABLE_NAME + "/#", Contract.FavoriteMovies.MOVIE_ID_CODE);
    }


    @Override
    public boolean onCreate() {
        helper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        int match = matcher.match(uri);
        Cursor result = null;
        db = helper.getReadableDatabase();

        switch (match){
            case Contract.FavoriteMovies.TABLE_CODE: case Contract.FavoriteMovies.MOVIE_ID_CODE:
               result = db.query(Contract.FavoriteMovies.TABLE_NAME,strings,s,strings1,null,null,s1);
        }
        result.setNotificationUri(getContext().getContentResolver(),uri);
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
        int match = matcher.match(uri);
        db = helper.getWritableDatabase();

        switch (match){
            case Contract.FavoriteMovies.TABLE_CODE:
                db.insert(Contract.FavoriteMovies.TABLE_NAME,null,contentValues);
        }
        return Uri.withAppendedPath(uri,contentValues.getAsString(Contract.FavoriteMovies._ID));
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int match = matcher.match(uri);
        db = helper.getWritableDatabase();
        switch(match){
            case Contract.FavoriteMovies.MOVIE_ID_CODE:
                db.delete(Contract.FavoriteMovies.TABLE_NAME,s,strings);
        }
        return 1;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

