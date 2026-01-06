package org.example.services;

import org.example.databases.Database;
import org.example.models.Rating;

import java.util.List;

public class RatingService {

    public void rate(Rating rating, int userId) {
        Database.upsertRating(userId, rating);
    }

    public void delete(int id, int userId) {
        Database.deleteRating(id, userId);
    }

    public List<Rating> getByMedia(int mediaId) {
        return Database.getRatingsForMedia(mediaId);
    }

    public List<Rating> getRatingsByUser(int userId)
    {
        return Database.getRatingsByUser(userId);
    }
}
