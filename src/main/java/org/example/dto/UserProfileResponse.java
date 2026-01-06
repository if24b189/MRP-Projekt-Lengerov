package org.example.dto;

public class UserProfileResponse {

    public int id;
    public String username;

    public int ratingCount;
    public int favoriteCount;

    public UserProfileResponse(int id, String username,
                               int ratingCount, int favoriteCount) {
        this.id = id;
        this.username = username;
        this.ratingCount = ratingCount;
        this.favoriteCount = favoriteCount;
    }
}
