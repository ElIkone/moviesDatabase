package com.tanxe.benja.moviesdatabase;

import com.tanxe.benja.moviesdatabase.database.AppDatabase;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
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
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecycleAdapter mRecycleAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<Movie> mMovieItems = new ArrayList<>();
    private ArrayList<Movie> topRated = new ArrayList<>();
    private final String TAG = MainActivity.class.getSimpleName();
    private boolean showFavModel = false;
    AppDatabase mdb;
    boolean showPopularModel = true;
    boolean showTopModel = false;
    GridLayoutManager mLayoutManager;
    private DayOfWeek ViewModelProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.benja.moviesdatabase.R.layout.activity_main);
        List<Movie> movies = new ArrayList<>();
        mLayoutManager = new GridLayoutManager(this, 2);

        mRecyclerView = findViewById(com.example.benja.moviesdatabase.R.id.recyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecycleAdapter = new RecycleAdapter(MainActivity.this);
        mRecyclerView.setAdapter(mRecycleAdapter);

        if (savedInstanceState == null) {
            if (isNetworkAvailable()) {
                URL searchUrl = Network.buildUrl("popular", " ");
                new GetQueryTask().execute(searchUrl);
                mRecycleAdapter.setMovieList(movies);
            } else {
                Toast.makeText(MainActivity.this, "Network is down", Toast.LENGTH_SHORT).show();
            }
        } else if (savedInstanceState.containsKey("currentPage")) {
            String currentPage = savedInstanceState.getString("currentPage");
            int position = savedInstanceState.getInt("currentPosition");
            if (currentPage == "favorites") {
                setupViewModel();
                mRecyclerView.scrollToPosition(position);

            } else if (currentPage == "top") {
                getTopMovies();
                mRecyclerView.smoothScrollToPosition(position);
            } else if (currentPage == "popular") {

                getPopularMovies();
                mRecyclerView.smoothScrollToPosition(position);
            }
        }
        mdb = AppDatabase.getsInstance((getApplicationContext()));
        setupViewModel();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String currentModel;
        int position;
        if (showPopularModel) {
            currentModel = "popular";
            position =  mLayoutManager.findFirstVisibleItemPosition();

        } else if (showFavModel) {
            currentModel = "favorites";
            position =  mLayoutManager.findFirstVisibleItemPosition();
        } else {
            currentModel = "top";
            position =  mLayoutManager.findFirstVisibleItemPosition();
        }

        outState.putString("currentPage", currentModel);
        outState.putInt("currentPosition", position);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        String currentPage = outState.getString("currentPage");
        if (currentPage == "top") {
            showFavModel = false;
            showTopModel = true;
            showPopularModel = false;
        } else if (currentPage == "favorites") {
            showFavModel = true;
            showTopModel = false;
            showPopularModel = false;
            setupViewModel();
        } else if (currentPage == "popular") {
            showFavModel = false;
            showTopModel = false;
            showPopularModel = true;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(com.example.benja.moviesdatabase.R.menu.settings, menu);
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


    private void setupViewModel() {
//        viewModel = ViewModelProvider(this).get(BaseViewModel.class);
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getTasks().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> moviesFavorites) {
               if (showFavModel) {
                    mRecycleAdapter.setMovieList(moviesFavorites);
                    if (moviesFavorites.size() == 0) {
                        Toast.makeText(MainActivity.this, "Network is down", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



    private void getMoviesModel() {
        final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        LiveData<List<Movie>> favorites = viewModel.getTasks();

        if (favorites.getValue().size() == 0) {
            Toast.makeText(MainActivity.this, "Favorites option doesn't have any movie", Toast.LENGTH_SHORT).show();
        } else {
            setupViewModel();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {
        switch (item.getItemId()) {
            case com.example.benja.moviesdatabase.R.id.action_settings:
                showFavModel = false;
                showPopularModel = true;
                showTopModel = false;
                getPopularMovies();
                return true;
            case com.example.benja.moviesdatabase.R.id.action_rated:
                showFavModel = false;
                showPopularModel = false;
                showTopModel = true;
                getTopMovies();
                return true;
            case com.example.benja.moviesdatabase.R.id.favorites:
                showFavModel = true;
                showTopModel = false;
                showPopularModel = false;
                getMoviesModel();
                return true;
        }
        return false;
    }
}