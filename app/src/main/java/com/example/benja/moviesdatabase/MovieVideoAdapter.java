package com.example.benja.moviesdatabase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import android.content.ActivityNotFoundException;

public class MovieVideoAdapter extends  RecyclerView.Adapter<MovieVideoAdapter.ViewHolder> {
    private final List<Video> mMovieVideos;
    private Context mContext;

    public MovieVideoAdapter(List<Video> items) {
        mMovieVideos = items;
    }

    @Override
    public int getItemCount() {
        return (mMovieVideos == null) ? 0 : mMovieVideos.size();
    }

    @Override
    public MovieVideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_video, parent, false);
        return new MovieVideoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Video movieVideo;
        public TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = itemView.findViewById(R.id.textView);
        }

        void bind(int listIndex) {
            movieVideo = mMovieVideos.get(listIndex);
        }

        @Override
        public void onClick(View view) {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + movieVideo.getKey()));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + movieVideo.getKey()));
            try {
                mContext.startActivity(webIntent);
            } catch (ActivityNotFoundException ex) {
                mContext.startActivity(appIntent);
            }
        }
    }
}