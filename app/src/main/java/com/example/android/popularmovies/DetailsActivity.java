package com.example.android.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final Movie movie = getIntent().getParcelableExtra(Movie.MOVIE_PARCEL_TAG);
        populate(movie);

        TextView trailerTV = findViewById(R.id.watch_trailers_tv);
        TextView reviewTV = findViewById(R.id.read_reviews_tv);
        final Button addToFavB = findViewById(R.id.add_to_fav_button);

        if (isFavorite(movie.getId())) addToFavB.setText(R.string.remove_from_fav_button);

        addToFavB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!isFavorite(movie.getId())) {
                    DBUtils.addMovie(getContentResolver(), movie);
                    addToFavB.setText(R.string.remove_from_fav_button);
                }
                else{
                    DBUtils.removeMovie(getContentResolver(), movie.getId());
                    addToFavB.setText(R.string.add_to_fav_button);

                }
                getContentResolver().notifyChange(Contract.FavoriteMovies.TABLE_URI,null);
            }
        });
        trailerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, TrailersActivity.class);
                intent.putExtra(Movie.MOVIE_ID_TAG, movie.getId() + "");
                startActivity(intent);
            }
        });

        reviewTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, ReviewsActivity.class);
                intent.putExtra(Movie.MOVIE_ID_TAG, movie.getId() + "");
                startActivity(intent);
            }
        });


    }

    void populate(Movie movie) {
        ImageView posterThumbnail = findViewById(R.id.movie_poster_thumbnail_iv);
        TextView titleRatingRelease = findViewById(R.id.title_rating_release_tv);
        TextView synopsisTV = findViewById(R.id.synopsis_tv);

        String originalTitle = movie.getOriginalTitle()
                .equals("") ? "???" : movie.getOriginalTitle();

        String releaseDate = movie.getReleaseDate()
                .equals("") ? "???" : movie.getReleaseDate();

        String synopsis = movie.getSynopsis()
                .equals("") ? "???" : movie.getSynopsis();


        Picasso.with(this).load(MovieNetworkUtils.buildPosterURL(movie.getPosterPath())).
                error(R.drawable.ic_launcher_background).into(posterThumbnail);

        titleRatingRelease.setText(
                String.format(getString(R.string.title_rating_release),
                        originalTitle, movie.getRating(), releaseDate));

        synopsisTV.setText(String.format(getString(R.string.synopsis), synopsis));

    }

    boolean isFavorite(int movieId){
        Cursor c = DBUtils.queryMovie(getContentResolver(),movieId);
        boolean result = c.getCount() != 0;
        c.close();
        return result ;
    }
}
