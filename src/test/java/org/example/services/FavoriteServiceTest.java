package org.example.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FavoriteServiceTest {

    @Test
    void favoriteServiceCanBeCreated() {
        FavoriteService service = new FavoriteService();
        assertNotNull(service);
    }

    @Test
    void addFavoriteDoesNotThrow() {
        FavoriteService service = new FavoriteService();
        assertDoesNotThrow(() -> service.add(1, 1));
    }

    @Test
    void removeFavoriteDoesNotThrow() {
        FavoriteService service = new FavoriteService();
        assertDoesNotThrow(() -> service.remove(1, 1));
    }
}
