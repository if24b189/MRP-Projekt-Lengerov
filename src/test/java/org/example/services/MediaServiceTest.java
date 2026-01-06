package org.example.services;

import org.example.models.Media;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MediaServiceTest {

    @Test
    void mediaStoresTitle() {
        Media m = new Media(1, "Matrix", "Desc", "SciFi", "MOVIE", 16, 1);
        assertEquals("Matrix", m.getTitle());
    }

    @Test
    void mediaStoresGenre() {
        Media m = new Media(1, "X", "Y", "Drama", "MOVIE", 12, 1);
        assertEquals("Drama", m.getGenre());
    }

    @Test
    void mediaStoresType() {
        Media m = new Media(1, "X", "Y", "Drama", "SERIES", 12, 1);
        assertEquals("SERIES", m.getType());
    }

    @Test
    void mediaStoresAgeRestriction() {
        Media m = new Media(1, "X", "Y", "Drama", "MOVIE", 18, 1);
        assertEquals(18, m.getAgeRestriction());
    }
}
