package com.tanxe.benja.moviesdatabase.database;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "task")

public class TaskEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String poster;
    private String release_date;
    private String average;
    private String mid;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }


    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public String getPoster() {
        return poster;
    }


    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Ignore
    public TaskEntry(String title, String poster, String description, String release_date, String average) {
        this.title = title;
        this.poster = poster;
        this.description = description;
        this.release_date = release_date;
        this.average = average;
    }

    public TaskEntry(String title, String poster, String release_date, String description, String average, String mid) {
        this.title = title;
        this.poster = poster;
        this.description = description;
        this.release_date = release_date;
        this.average = average;
        this.mid = mid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}