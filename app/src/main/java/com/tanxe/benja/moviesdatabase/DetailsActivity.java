package com.tanxe.benja.moviesdatabase;

import android.app.AlertDialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.tanxe.benja.moviesdatabase.database.AppDatabase;
import com.tanxe.benja.moviesdatabase.database.TaskEntry;
import com.squareup.picasso.Picasso;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "movie";
    public String year2;
    public String movieid;
    private List<Video> mMovieVideos = new ArrayList<>();
    public Review mMovieReview;
    private RecyclerView mRecyclerView;
    TextView year;
    ImageView poster;
    TextView title;
    TextView description;
    TextView average;
    Button review;
    ImageView starImage;
    MovieVideoAdapter movieVideoAdapter;
    private AppDatabase mDb;
    private TextView mMovieReviewDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.benja.moviesdatabase.R.layout.media_delegate);
        final Movie mMovie;
        mDb = AppDatabase.getsInstance(getApplicationContext());

        if (getIntent().hasExtra(EXTRA_MOVIE)) {
            mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        } else {
            throw new IllegalArgumentException("Detail activity must receive a movie parcelable");
        }

        final String poster2 = mMovie.getPoster2();
        final String averageOriginal = mMovie.getAverage();
        poster = findViewById(com.example.benja.moviesdatabase.R.id.imageView);
        starImage = findViewById(com.example.benja.moviesdatabase.R.id.star_icon);
        review = findViewById(com.example.benja.moviesdatabase.R.id.reviewButton);
        title = findViewById(com.example.benja.moviesdatabase.R.id.movieTittle);
        description = findViewById(com.example.benja.moviesdatabase.R.id.textSummary);
        year = findViewById(com.example.benja.moviesdatabase.R.id.textYear);
        average = findViewById(com.example.benja.moviesdatabase.R.id.textView2);
        title.setText(mMovie.getTitle());
        description.setText(mMovie.getDescription());
        year2 = mMovie.getYear();
        year2 = year2.substring(0, 4);
        year.setText(year2);
        average.setText(mMovie.getAverage() + "/10");
        movieid = mMovie.getMid();
        getVideos(movieid);
        mRecyclerView = findViewById(com.example.benja.moviesdatabase.R.id.videoRecycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieVideoAdapter = new MovieVideoAdapter(mMovieVideos);
        mRecyclerView.setAdapter(movieVideoAdapter);

        starImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFavorites(poster2,averageOriginal);
            }
        });

        setFavoriteIcon(movieid);

        Picasso.with(this)
                .load(mMovie.getPoster())
                .into(poster);

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getReview(movieid);
             }
            });
        }

    private void getReview(String id) {
        URL theMoviesUrl = Network.buildUrl("review", id);
        new TheMoviesGetReviews().execute(theMoviesUrl);
    }

    private void setFavoriteIcon(String mid ) {
        boolean isFavorite = mDb.taskDao().loadTaskById(mid) != null;

        if (isFavorite) {
            starImage.setImageResource(com.example.benja.moviesdatabase.R.drawable.filledstar);
        } else {
            starImage.setImageResource(com.example.benja.moviesdatabase.R.drawable.star);
        }

    }

    private class TheMoviesGetReviews extends AsyncTask<URL, String, Review> {
        @Override
        protected  Review doInBackground(URL... params) {
            try {
                Network networkUtils = new Network();
                mMovieReview = networkUtils.getVideoReview(movieid);
                return mMovieReview;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mMovieReview;
        }

        @Override
        protected void onPostExecute(Review mReview) {
            if (mReview != null) {
                mMovieReview = mReview;
                if (mMovieReview.getrReview() != null) {
                    showPopUp();
                    updateReview(mReview);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(com.example.benja.moviesdatabase.R.string.review), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
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

    private void showPopUp() {
        // Build an AlertDialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(com.example.benja.moviesdatabase.R.layout.dialog,null);

        // Specify alert dialog is not cancelable/not ignorable
        builder.setCancelable(false);
        mMovieReviewDesc = dialogView.findViewById(com.example.benja.moviesdatabase.R.id.author);
        // Set the custom layout as alert dialog view
        builder.setView(dialogView);

        Button closeButton = dialogView.findViewById(com.example.benja.moviesdatabase.R.id.closeButton);
        final AlertDialog dialog = builder.create();
        closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                dialog.setOnKeyListener(new Dialog.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        if (i == KeyEvent.KEYCODE_BACK) {
                            dialog.dismiss();
                        }
                        return true;
                    }
                });
    }

    private void updateReview(Review mReview) {
        mMovieReviewDesc.setText(mReview.getrReview());

    }

    public void saveFavorites(String poster2, String averageOriginal) {
        String title2 = title.getText().toString();
        String description2 = description.getText().toString();
        String release_date = year.getText().toString();
        String calif = averageOriginal;
        String mid2 = movieid;
        TaskEntry taskEntry = new TaskEntry(title2, poster2,release_date,description2,calif, mid2);
        checkFavorite(taskEntry);
    }

    public  void checkFavorite(TaskEntry taskEntry) {
        boolean isFav =  mDb.taskDao().loadTaskById(taskEntry.getMid()) != null;
        if (isFav) {
            mDb.taskDao().delete(taskEntry.getMid());
            Toast toast = Toast.makeText(getApplicationContext(), "Movie has been removed from Favorites", Toast.LENGTH_SHORT);
            toast.show();
            setFavoriteIcon(taskEntry.getMid());

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Movie has been added to Favorites", Toast.LENGTH_SHORT);
            toast.show();
            mDb.taskDao().insertTask(taskEntry);
            mDb.taskDao().loadAllTask();
            setFavoriteIcon(taskEntry.getMid());
        }
    }

    private class TheMoviesGetVideos extends AsyncTask<URL, String, List<Video>> {
        @Override
        protected List<Video> doInBackground(URL... params) {
            try {
                Network networkUtils = new Network();
                mMovieVideos = networkUtils.getPopularVideos(movieid);
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