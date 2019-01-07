package com.tanxe.benja.moviesdatabase;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.tanxe.benja.moviesdatabase.database.AppDatabase;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> movie;

    public MainViewModel(@NonNull Application application) {

        super(application);
        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        movie = database.taskDao().loadAllTask();
    }

    public LiveData<List<Movie>> getTasks() {
        return movie;
    }
}
