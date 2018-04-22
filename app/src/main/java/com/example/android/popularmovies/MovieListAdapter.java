package com.example.android.popularmovies;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private Movie[] mData;


    MovieListAdapter(Movie[] data) {
        mData = data;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(v);
    }

    void setmData(Movie[] mData) {
        this.mData = mData;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView moviePoster;

        MovieViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            moviePoster = itemView.findViewById(R.id.movie_poster_iv);

        }


        void bind(int itemPosition) {

            //todo Please check this...
            // Why are images being cached and loaded correctly offline , when
            // I am loading the url via picasso with s2?
            // why is this behavior not happening when I am loading with s1?
            // even though s1 is equal to s2........
            // some info would be very appreciated, thank you.
            String s1 = mData[itemPosition].getPosterThumbnailURL();
            String s2 = MovieNetworkUtils.buildPosterURL(mData[itemPosition].getPosterPath());
            boolean b = s1.equals(s2); //This outputs true...
            //------------------------------

            Picasso.with(moviePoster.getContext()).
                    load(s2)
                    .error(R.drawable.ic_launcher_background)
                    .into(moviePoster);
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(moviePoster.getContext(), DetailsActivity.class);
            intent.putExtra(Movie.MOVIE_PARCEL_TAG, mData[getAdapterPosition()]);
            moviePoster.getContext().startActivity(intent);
        }
    }
}
