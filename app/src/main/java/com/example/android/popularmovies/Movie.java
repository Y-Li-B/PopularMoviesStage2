package com.example.android.popularmovies;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

class Movie implements Parcelable {

    static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };

    static final String MOVIE_PARCEL_TAG = "MOVIE_PARCEL";
    static final String MOVIE_ID_TAG = "MOVIE_ID";



    private String originalTitle;
    private String posterThumbnailURL;
    private String synopsis;
    private int rating;
    private String releaseDate;
    private String posterPath;
    private int id;

    private String[] trailerKeys;


    private Movie(Parcel in) {
        originalTitle = in.readString();
        posterThumbnailURL = in.readString();
        synopsis = in.readString();
        rating = in.readInt();
        releaseDate = in.readString();
        posterPath = in.readString();
        id = in.readInt();
        trailerKeys = in.createStringArray();

    }

    Movie(String json) throws JSONException {
        JSONObject root = new JSONObject(json);

        originalTitle = root.getString("original_title");
        posterThumbnailURL = MovieNetworkUtils.buildPosterURL(root.getString("poster_path"));
        synopsis = root.getString("overview");
        rating = root.getInt("vote_average");
        releaseDate = root.getString("release_date");
        posterPath = root.getString("poster_path");
        id = root.getInt("id");

    }

     Movie(String originalTitle, String posterThumbnailURL, String synopsis, int rating, String releaseDate, String posterPath, int id) {
        this.originalTitle = originalTitle;
        this.posterThumbnailURL = posterThumbnailURL;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.id = id;
    }

    String getOriginalTitle() {
        return originalTitle;
    }

    String getPosterThumbnailURL() {
        return posterThumbnailURL;
    }

    String getSynopsis() {
        return synopsis;
    }

    int getRating() {
        return rating;
    }

    String getReleaseDate() {
        return releaseDate;
    }

    String getPosterPath() {
        return posterPath;
    }

    public int getId() {
        return id;
    }

    public void setTrailerKeys(String[] trailerKeys) {
        this.trailerKeys = trailerKeys;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(originalTitle);
        parcel.writeString(posterThumbnailURL);
        parcel.writeString(synopsis);
        parcel.writeInt(rating);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeInt(id);
        parcel.writeStringArray(trailerKeys);
    }
}
