package org.example.services;

import org.example.databases.Database;
import org.example.dto.UserProfileResponse;
import org.example.models.User;

public class UserService {

    // ---------- AUTH ----------
    public void register(User user) throws Exception {
        Database.createUser(user.getUsername(), user.getPassword());
    }

    public int login(User user) throws Exception {
        return Database.login(user.getUsername(), user.getPassword());
    }

    // ---------- INTERNAL ----------
    public User getUserById(int userId) {
        return Database.getUser(userId);
    }

    // ---------- PUBLIC PROFILE ----------
    public UserProfileResponse getProfile(int userId) {
        User u = Database.getUser(userId);

        int ratings = Database.countRatingsByUser(userId);
        int favorites = Database.countFavoritesByUser(userId);

        return new UserProfileResponse(
                u.getId(),
                u.getUsername(),
                ratings,
                favorites
        );
    }
}
