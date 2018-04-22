package com.example.android.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, Contract.DB_NAME, null, Contract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String s = "CREATE TABLE %s ( %s INTEGER, %s TEXT," +
                " %s TEXT, %s INTEGER," +
                " %s TEXT, %s TEXT," +
                " %s TEXT);";

        String CREATE_STATEMENT = String.format(s,
                Contract.FavoriteMovies.TABLE_NAME, Contract.FavoriteMovies._ID,
                Contract.FavoriteMovies.ORIGINAL_TITLE_COLULMN, Contract.FavoriteMovies.SYNOPOSIS_COLUMN,
                Contract.FavoriteMovies.RATING_COLUMN, Contract.FavoriteMovies.RELEASE_DATE_COLUMN,
                Contract.FavoriteMovies.POSTER_PATH_COLUMN, Contract.FavoriteMovies.POSTER_THUMBNAIL_URL_COLUMN);

        sqLiteDatabase.execSQL(CREATE_STATEMENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Needs to be changed if data loss in an issue, for now we are not concerned with upgrading.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.FavoriteMovies.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
