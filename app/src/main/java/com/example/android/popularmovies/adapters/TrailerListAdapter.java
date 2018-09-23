package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.utils.NetworkUtils;

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.TrailerHolder> {

    //todo rework this class
    private String[] mDataSet;
    private String[] mTrailerLabels;

    public TrailerListAdapter(String[] mDataSet) {
        this.mTrailerLabels = new String[mDataSet.length];

        for (int i = 0; i < mTrailerLabels.length; i++) {
            mTrailerLabels[i] = "Trailer " + (i + 1);
        }

        this.mDataSet = mDataSet;
    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer, parent, false);
        return new TrailerHolder(v);
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    public void setmDataSet(String[] mDataSet) {
        this.mDataSet = mDataSet;
    }

    class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView trailerTV;

        TrailerHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            trailerTV = v.findViewById(R.id.trailer_tv);
        }

        void bind(int position) {
            trailerTV.setText(mTrailerLabels[position]);
            trailerTV.setTag(R.string.movie_trailer_tag, mDataSet[position]);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    NetworkUtils.getYoutubeLink((String)view.getTag(R.string.movie_trailer_tag)));

            Context context = view.getContext();

            if(intent.resolveActivity(context.getPackageManager())!=null)
            context.startActivity(intent);
        }
    }
}
