package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class Media {
    private final int id;
    private final String title;
    private final String description;

    private final String genre;
    private final String type;
    private final int ageRestriction;

    private final int createdBy;

    @JsonIgnore
    private final LocalDateTime createdAt;

    private final double averageRating;

    public Media(
            int id,
            String title,
            String description,
            String genre,
            String type,
            int ageRestriction,
            int createdBy
    ) {
        this(
                id,
                title,
                description,
                genre,
                type,
                ageRestriction,
                createdBy,
                LocalDateTime.now(),
                0.0
        );
    }

    public Media(
            int id,
            String title,
            String description,
            String genre,
            String type,
            int ageRestriction,
            int createdBy,
            LocalDateTime createdAt,
            double averageRating
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.type = type;
        this.ageRestriction = ageRestriction;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.averageRating = averageRating;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }

    public String getGenre() { return genre; }
    public String getType() { return type; }
    public int getAgeRestriction() { return ageRestriction; }

    public int getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public double getAverageRating() { return averageRating; }

    @Override
    public String toString() {
        return "Media{id=" + id + ", title='" + title + "', type='" + type + "'}";
    }
}
