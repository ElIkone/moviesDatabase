package com.example.benja.moviesdatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "movie";
    private String year2;
    TextView year;
    ImageView poster;
    TextView title;
    TextView description;
    TextView average;
    Button favorites;

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

        title.setText(mMovie.getTitle());
        description.setText(mMovie.getDescription());
        year2 = mMovie.getYear();
        year2 = year2.substring(0, 4);
        year.setText(year2);
        average.setText(mMovie.getAverage() + "/10");

        Picasso.with(this)
                .load(mMovie.getPoster())
                .into(poster);
    }
}