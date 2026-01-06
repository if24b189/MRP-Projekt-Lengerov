package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

public class Rating {

    private int id;
    private int mediaId;

    @JsonIgnore
    private int userId;

    private int rating;
    private String comment;

    @JsonIgnore
    private LocalDateTime createdAt;

    public Rating() {}

    public int getId() {
        return id;
    }

    public int getMediaId() {
        return mediaId;
    }

    public int getUserId() {
        return userId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
