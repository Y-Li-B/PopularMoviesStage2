package com.example.android.popularmovies.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.content.Contract;

//This class will be used to deal with the SQLite database.
public class DBHelper extends SQLiteOpenHelper {

     DBHelper(Context context) {
        super(context, Contract.DB_NAME, null, Contract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createStatement = "CREATE TABLE %s ( %s INTEGER, %s TEXT," +
                " %s TEXT, %s INTEGER," +
                " %s TEXT, %s TEXT," +
                " %s TEXT);";

         createStatement = String.format(createStatement,
                Contract.FavoriteMovies.TABLE_NAME, Contract.FavoriteMovies._ID,
                Contract.FavoriteMovies.ORIGINAL_TITLE_COLUMN, Contract.FavoriteMovies.SYNOPSIS_COLUMN,
                Contract.FavoriteMovies.RATING_COLUMN, Contract.FavoriteMovies.RELEASE_DATE_COLUMN,
                Contract.FavoriteMovies.POSTER_PATH_COLUMN, Contract.FavoriteMovies.POSTER_THUMBNAIL_URL_COLUMN);

        sqLiteDatabase.execSQL(createStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Needs to be changed if data loss in an issue, for now we are not concerned with upgrading.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.FavoriteMovies.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
