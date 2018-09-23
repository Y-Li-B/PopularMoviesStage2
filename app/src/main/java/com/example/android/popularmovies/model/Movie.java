package com.example.android.popularmovies.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.popularmovies.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie implements Parcelable {

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

    public static final String PARCEL_TAG = "movie_parcel";
    public static final String ID_TAG = "movie_id";

    private String originalTitle;
    private String posterThumbnailURL;
    private String synopsis;
    private int rating;
    private String releaseDate;
    private String posterPath;
    private int id;

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(originalTitle);
        parcel.writeString(posterThumbnailURL);
        parcel.writeString(synopsis);
        parcel.writeInt(rating);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeInt(id);
    }

    private Movie(Parcel in) {
        originalTitle = in.readString();
        posterThumbnailURL = in.readString();
        synopsis = in.readString();
        rating = in.readInt();
        releaseDate = in.readString();
        posterPath = in.readString();
        id = in.readInt();

    }

    public Movie(String json) throws JSONException {

        JSONObject root = new JSONObject(json);

        originalTitle = root.getString("original_title");
        posterThumbnailURL = NetworkUtils.buildPosterURL(root.getString("poster_path"));
        synopsis = root.getString("overview");
        rating = root.getInt("vote_average");
        releaseDate = root.getString("release_date");
        posterPath = root.getString("poster_path");
        id = root.getInt("id");

    }

    public Movie(String originalTitle, String posterThumbnailURL, String synopsis, int rating, String releaseDate, String posterPath, int id) {
        this.originalTitle = originalTitle;
        this.posterThumbnailURL = posterThumbnailURL;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterThumbnailURL() {
        return posterThumbnailURL;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public int getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
