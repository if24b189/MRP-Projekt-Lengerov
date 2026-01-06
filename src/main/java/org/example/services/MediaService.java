package org.example.services;

import org.example.databases.Database;
import org.example.models.Media;

import java.util.List;

public class MediaService {

    public void create(Media media, int ownerId) {
        Database.createMedia(media, ownerId);
    }

    public List<Media> getAll() {
        return Database.getAllMedia();
    }

    public void update(int id, Media media, int userId) {
        Database.updateMedia(id, media, userId);
    }

    public void delete(int id, int userId) {
        Database.deleteMedia(id, userId);
    }
}
