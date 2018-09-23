package com.example.android.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.content.Contract;
import com.example.android.popularmovies.content.DBUtils;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView trailerTV = findViewById(R.id.watch_trailers_tv);
        TextView reviewTV = findViewById(R.id.read_reviews_tv);
        final Button favoriteButton = findViewById(R.id.add_to_fav_button);

        final Movie movie = getIntent().getParcelableExtra(Movie.PARCEL_TAG);

        populate(movie);

        initFavButton(favoriteButton,movie);

        trailerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, TrailersActivity.class);
                intent.putExtra(Movie.ID_TAG, movie.getId() + "");
                startActivity(intent);
            }
        });

        reviewTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, ReviewsActivity.class);
                intent.putExtra(Movie.ID_TAG, movie.getId() + "");
                startActivity(intent);
            }
        });


    }

    void initFavButton(final Button favoriteButton,final Movie movie){

        if (isFavorite(movie.getId())) favoriteButton.setText(R.string.remove_from_fav_button);

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFavorite(movie.getId())) {
                    DBUtils.addMovie(getContentResolver(), movie);
                    favoriteButton.setText(R.string.remove_from_fav_button);
                } else {
                    DBUtils.removeMovie(getContentResolver(), movie.getId());
                    favoriteButton.setText(R.string.add_to_fav_button);

                }
                getContentResolver().notifyChange(Contract.FavoriteMovies.TABLE_URI, null);
            }
        });

    }

    void populate(Movie movie) {
        ImageView posterThumbnail = findViewById(R.id.movie_poster_thumbnail_iv);
        TextView titleRatingRelease = findViewById(R.id.title_rating_release_tv);
        TextView synopsisTV = findViewById(R.id.synopsis_tv);


        String originalTitle = movie.getOriginalTitle().isEmpty()
                ? "???" : movie.getOriginalTitle();

        String releaseDate = movie.getReleaseDate().isEmpty()
                ? "???" : movie.getReleaseDate();

        String synopsis = movie.getSynopsis().isEmpty()
                ? "???" : movie.getSynopsis();


        Picasso.with(this).load(NetworkUtils.buildPosterURL(movie.getPosterPath())).
                error(R.drawable.ic_launcher_background).into(posterThumbnail);

        titleRatingRelease.setText(
                getString(R.string.title_rating_release,
                        originalTitle, movie.getRating(), releaseDate));

        synopsisTV.setText(getString(R.string.synopsis, synopsis));

    }

    boolean isFavorite(int movieId) {
        Cursor c = DBUtils.queryMovie(getContentResolver(), movieId);
        boolean result = c.getCount() != 0;
        c.close();
        return result;
    }
}
