package com.example.android.popularmovies;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    static final String DB_NAME = "movies.db";
    static final int DB_VERSION = 1;
    static final String AUTHORITY =  "com.example.android.popularmovies.provider";
    static final String BASE_URI = "content://" + AUTHORITY;

    static final class FavoriteMovies implements BaseColumns {

        static final String TABLE_NAME = "favorites";
        static final Uri TABLE_URI = Uri.parse(BASE_URI).buildUpon().appendPath(TABLE_NAME).build();
        static final int TABLE_CODE = 100 ;
        static final int MOVIE_ID_CODE = 101;

        static final String ORIGINAL_TITLE_COLULMN = "ORIGINAL_TITLE";
        static final String POSTER_THUMBNAIL_URL_COLUMN = "POSTER_THUMBNAIL_URL";
        static final String SYNOPOSIS_COLUMN = "SYNOPSIS";
        static final String RATING_COLUMN = "RATING";
        static final String RELEASE_DATE_COLUMN = "RELEASE_DATE";
        static final String POSTER_PATH_COLUMN = "POSTER_PATH";
    }
}
