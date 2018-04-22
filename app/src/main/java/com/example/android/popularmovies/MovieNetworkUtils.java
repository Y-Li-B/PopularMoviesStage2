package com.example.android.popularmovies;


import android.net.Uri;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class MovieNetworkUtils {

    private final static String BASE_URL = "https://api.themoviedb.org/3";
    private final static String API_KEY_PARAM = "api_key";
    //TODO API KEY FIELD IS HERE!
    private final static String API_KEY = "";
    //Sort orders of the movie query.
    final static String SORT_ORDER_POPULAR = "/movie/popular";
    final static String SORT_ORDER_HIGHEST_RATED = "/movie/top_rated";


    static String buildPosterURL(String path) {
        final String BASE_URL = "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE = "w342";

        Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath(IMAGE_SIZE).build();

        return uri.toString() + path;
    }


    /**
     * Builds a URL which can later be used to a "discover" query, which is used to get a list of
     * movies depending on the supplied sort order.
     *
     * @param sortOrder the sort order of the movies in the query.
     * @return String | Null returns either a String URL or returns null if an exception occurs
     */

    static private String buildDiscoverURL(String sortOrder) {

        final String DISCOVER_BASE_URL = BASE_URL + sortOrder;

        Uri uri = Uri.parse(DISCOVER_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY).build();

        return uri.toString();
    }


    static private String buildMovieTrailerURL(String movieId) {
        final String TRAILER_BASE_URL = String.format("%s/movie/%s/videos", BASE_URL, movieId);
        Uri uri = Uri.parse(TRAILER_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        return uri.toString();

    }

    static private String buildMovieReviewURL(String movieId){
        final String REVIEW_BASE_URL = String.format("https://api.themoviedb.org/3/movie/%s/reviews",movieId);
        Uri uri = Uri.parse(REVIEW_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM,API_KEY).build();
        return uri.toString();
    }

    static private Review[] parseReviewRespone(String json)throws IOException,JSONException{
        if (json != null) {
            JSONObject root = new JSONObject(json);
            JSONArray results = root.getJSONArray("results");

            Review[] reviewList = new Review[results.length()];

            for (int i = 0; i < results.length(); i++) {
                JSONObject jsonReview = results.getJSONObject(i);
                reviewList[i] = new Review(jsonReview.getString("author")
                        ,jsonReview.getString("content"));
            }

            return reviewList;
        } else {
            return null;
        }

    }

    /**
     * Gets a json string from the provided url string...
     *
     * @param url the URL to get the response from...
     * @return String | null returns a json string when receiving response correctly , else will return
     * null if something goes wrong.
     */
    static private String getResponse(String url) throws IOException {

        //Make a new OkHTTP client...
        OkHttpClient client = new OkHttpClient();

        //Make a request object for the client to call later using the call() method...
        Request request = new Request.Builder().url(url).build();

        //Perform the request you want which should return a response object...
        Response response = client.newCall(request).execute();

        //Get the response body...
        ResponseBody body = response.body();

        //Get the string from the response body...
        return body == null ? null : body.string();

    }

    /**
     * Takes a json String and parses it into an array of strings
     *
     * @param json a json String to parse into a string array
     * @return String[] | null a string array containing movie js objects or null if
     * there was something wrong with the json
     */
    static private Movie[] parseMovieListResponse(String json) throws JSONException {

        if (json != null) {
            JSONObject root = new JSONObject(json);
            JSONArray results = root.getJSONArray("results");

            Movie[] movieList = new Movie[results.length()];

            for (int i = 0; i < results.length(); i++) {
                movieList[i] = new Movie(results.getJSONObject(i).toString());
            }

            return movieList;
        } else {
            return null;
        }
    }

    private static String[] parseTrailerListResponse (String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONArray results = root.getJSONArray("results");

        String[] trailerKeys = new String[results.length()];

        for (int i = 0 ; i < results.length() ; i ++ ){
            trailerKeys[i] = results.getJSONObject(i).getString("key");
        }
        return trailerKeys;
    }

    //Build, get a response , then parse and return.
    static Movie[] getMovieList(String sortOrder) throws IOException, JSONException{
        String discoverUrl = buildDiscoverURL(sortOrder);
        String movieListResponse = getResponse(discoverUrl);
        return parseMovieListResponse(movieListResponse);
    }

    //Build, get a response , then parse and return.
    static String[] getTrailerKeys(String movieId) throws IOException, JSONException{
        String trailersUrl = buildMovieTrailerURL(movieId);
        String trailerListResponse = getResponse(trailersUrl);
        return parseTrailerListResponse(trailerListResponse);
    }

    static Uri getYoutubeLink(String movieKey) {
        final String BASE_URL = "https://www.youtube.com/watch";

        return Uri.parse(BASE_URL).buildUpon().appendQueryParameter("v", movieKey).build();
    }

    static Review[] getReviews(String movieId) throws IOException, JSONException {
        String reviewUrl = buildMovieReviewURL(movieId);
        String respone = getResponse(reviewUrl);
        return parseReviewRespone(respone);

    }

    }

