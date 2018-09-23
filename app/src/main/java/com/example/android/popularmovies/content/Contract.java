package com.example.android.popularmovies.content;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {
    //The name of the database.
    static final String DB_NAME = "movies.db";
    //The current database version.
    static final int DB_VERSION = 1;

    static final public class FavoriteMovies implements BaseColumns {

        static final String TABLE_NAME = "favorites";
        public static final Uri TABLE_URI = Uri.parse(PopularMoviesContentProvider.BASE_URI).buildUpon().appendPath(TABLE_NAME).build();

        /*These are integer codes which represents the table, they are used in cases such as when
         using the URI matcher in the content provider, which matches a certain URI with it's
        corresponding code*/
        static final int TABLE_CODE = 100 ;
        static final int MOVIE_ID_CODE = 101;

        //constant for column names.
        public static final String ORIGINAL_TITLE_COLUMN = "ORIGINAL_TITLE";
        public static final String POSTER_THUMBNAIL_URL_COLUMN = "POSTER_THUMBNAIL_URL";
        public static final String SYNOPSIS_COLUMN = "SYNOPSIS";
        public static final String RATING_COLUMN = "RATING";
        public static final String RELEASE_DATE_COLUMN = "RELEASE_DATE";
        public static final String POSTER_PATH_COLUMN = "POSTER_PATH";
    }
}
