package com.example.benja.moviesdatabase;

public class Review {
    private String rAuthor;
    private String rReview;

    public String getrAuthor() {
        return rAuthor;
    }

    public void setrAuthor(String rAuthor) {
        this.rAuthor = rAuthor;
    }

    public String getrReview() {
        return rReview;
    }

    public void setrReview(String rReview) {
        this.rReview = rReview;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    private String rId;
}
