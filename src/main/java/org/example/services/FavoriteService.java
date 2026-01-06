package org.example.services;

import org.example.databases.Database;
import org.example.models.Media;

import java.util.List;

public class FavoriteService {

    public void add(int userId, int mediaId) {
        Database.addFavorite(userId, mediaId);
    }

    public void remove(int userId, int mediaId) {
        Database.removeFavorite(userId, mediaId);
    }

    public List<Media> getAll(int userId) {
        return Database.getFavorites(userId);
    }
}
