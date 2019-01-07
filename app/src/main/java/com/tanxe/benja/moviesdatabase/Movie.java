package com.tanxe.benja.moviesdatabase;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @SerializedName("id")
    private String id;
    private String title;
    @SerializedName("poster_path")
    private String poster;
    @SerializedName("overview")
    private String description;
    @SerializedName("release_date")
    private String release_date;
    @SerializedName("vote_average")
    private String average;
    private  String mid;
    private  String url_image = "http://image.tmdb.org/t/p/w500";

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    @SerializedName("mid")

    public String getRelease_date() {
        return release_date;
    }

    public String getUrl_image() {
        return url_image;
    }


    public Movie () {

    }


    public Movie(Parcel in) {
        id = in.readString();
        title = in.readString();
        poster = in.readString();
        description = in.readString();
        release_date = in.readString();
        average = in.readString();
        mid = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return url_image + poster;
    }

    public String getPoster2() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {return id; }

    public void setId(String id) { this.id = id; }

    public String getYear() {
        return release_date;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
    public void url_image(String url_image) {
        this.url_image = url_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(poster);
        parcel.writeString(description);
        parcel.writeString(release_date);
        parcel.writeString(average);
        parcel.writeString(mid);
    }
}