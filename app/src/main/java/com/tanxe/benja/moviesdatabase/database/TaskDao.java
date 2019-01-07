package com.tanxe.benja.moviesdatabase.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.tanxe.benja.moviesdatabase.Movie;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    LiveData<List<Movie>>loadAllTask();

    @Insert
    void insertTask(TaskEntry taskEntry);

    @Query("DELETE FROM task WHERE mid = :mid")
    void   delete( String mid);

    @Query("SELECT * FROM task WHERE mid = :mid")
    TaskEntry loadTaskById(String mid);
}
