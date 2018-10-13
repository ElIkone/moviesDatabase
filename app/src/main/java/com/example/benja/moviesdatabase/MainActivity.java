package com.example.benja.moviesdatabase;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    private RecycleAdapter mRecycleAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<Movie> mMovieItems = new ArrayList<>();
    private ArrayList<Movie> topRated = new ArrayList<>();
    private final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Movie> movies = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecycleAdapter = new RecycleAdapter(MainActivity.this);
        mRecyclerView.setAdapter(mRecycleAdapter);
        if (isNetworkAvailable()) {
            URL searchUrl = Network.buildUrl("popular", " ");
            new GetQueryTask().execute(searchUrl);
            mRecycleAdapter.setMovieList(movies);
        } else {
            Toast.makeText(MainActivity.this, "Network is down", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo netWorkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (netWorkInfo != null && netWorkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }
    private class GetQueryTask extends AsyncTask<URL, Void, ArrayList<Movie>> {
        @Override
        protected ArrayList<Movie> doInBackground(URL... params) {
            try {
                Network networkUtils = new Network();
                mMovieItems = networkUtils.getPopularMovies();
                return mMovieItems;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mMovieItems;
        }
        @Override
        protected void onPostExecute(ArrayList<Movie> newMovieItems) {
            if (mMovieItems != null && mMovieItems.size() > 0) {
                mMovieItems = newMovieItems;
                setupAdapter();
            } else {
            }
        }
    }
    private class GetTopQueryTask extends AsyncTask<URL, Void, ArrayList<Movie>> {
        @Override
        protected ArrayList<Movie> doInBackground(URL... params) {
            try {
                Network networkUtils = new Network();
                topRated = networkUtils.getTopMovies();
                return topRated;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return topRated;
        }
        @Override
        protected void onPostExecute(ArrayList<Movie> newMovieItems) {
            if (topRated != null && topRated.size() > 0) {
                topRated = newMovieItems;
                Log.d(TAG, " postExecute");
                setupAdapterRated();
            } else {
            }
        }
    }
    private void setupAdapter() {
        mRecycleAdapter.setMovieList(mMovieItems);
    }
    private void setupAdapterRated() {
        mRecycleAdapter.setMovieList(topRated);
    }
    private void getPopularMovies() {
        URL searchUrl = Network.buildUrl("popular", " ");
        new GetQueryTask().execute(searchUrl);
    }
    private void getTopMovies() {
        URL searchUrl = Network.buildUrl("rated", " ");
        new GetTopQueryTask().execute(searchUrl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {
        switch (item.getItemId()) {
            case R.id.action_settings:
                getPopularMovies();
                return true;
            case R.id.action_rated:
                getTopMovies();
                return true;
        }
        return false;
    }
}