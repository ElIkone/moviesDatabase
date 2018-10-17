package com.example.benja.moviesdatabase;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.LinearLayoutManager;
import com.squareup.picasso.Picasso;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "movie";
    public String year2;
    public String mId;
    private List<Video> mMovieVideos = new ArrayList<>();
    private RecyclerView mRecyclerView;
    TextView year;
    ImageView poster;
    TextView title;
    TextView description;
    TextView average;
    Button favorites;
    MovieVideoAdapter movieVideoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_delegate);


        Movie mMovie;

        if (getIntent().hasExtra(EXTRA_MOVIE)) {
            mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        } else {
            throw new IllegalArgumentException("Detail activity must receive a movie parcelable");
        }

        poster = findViewById(R.id.imageView);
        favorites = findViewById(R.id.favoritesButton);
        title = findViewById(R.id.movieTittle);
        description = findViewById(R.id.textSummary);
        year = findViewById(R.id.textYear);
        average = findViewById(R.id.textView2);
        mId = mMovie.getId();

        title.setText(mMovie.getTitle());
        description.setText(mMovie.getDescription());
        year2 = mMovie.getYear();
        year2 = year2.substring(0, 4);
        year.setText(year2);
        average.setText(mMovie.getAverage() + "/10");
        getVideos(mId);
        mRecyclerView = findViewById(R.id.videoRecycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieVideoAdapter = new MovieVideoAdapter(mMovieVideos);
        mRecyclerView.setAdapter(movieVideoAdapter);

        Picasso.with(this)
                .load(mMovie.getPoster())
                .into(poster);
    }

    private void setupAdapter() {
        MovieVideoAdapter movieVideoAdapter = new MovieVideoAdapter(mMovieVideos);
        mRecyclerView.setAdapter(movieVideoAdapter);
    }

    private void getVideos(String id) {
        Network networkUtils = new Network();
        URL searchUrl = networkUtils.buildUrl("videos", id);
        new TheMoviesGetVideos().execute(searchUrl);
    }

    private class TheMoviesGetVideos extends AsyncTask<URL, String, List<Video>> {
        @Override
        protected List<Video> doInBackground(URL... params) {
            try {
                Network networkUtils = new Network();
                mMovieVideos = networkUtils.getPopularVideos(mId);
                return mMovieVideos;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mMovieVideos;
        }

        @Override
        protected void onPostExecute(List<Video> newMovieItems) {
            if (mMovieVideos != null && mMovieVideos.size() > 0) {
                mMovieVideos = newMovieItems;
                setupAdapter();
            }
        }
    }
}