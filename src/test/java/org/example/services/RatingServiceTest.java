package org.example.services;

import org.example.models.Rating;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RatingServiceTest {

    @Test
    void ratingStoresRatingValue() {
        Rating r = new Rating();
        setPrivateField(r, "rating", 5);

        assertEquals(5, r.getRating());
    }

    @Test
    void ratingStoresMediaId() {
        Rating r = new Rating();
        setPrivateField(r, "mediaId", 99);

        assertEquals(99, r.getMediaId());
    }

    @Test
    void ratingStoresUserId() {
        Rating r = new Rating();
        r.setUserId(77);

        assertEquals(77, r.getUserId());
    }

    @Test
    void ratingStoresComment() {
        Rating r = new Rating();
        setPrivateField(r, "comment", "great");

        assertEquals("great", r.getComment());
    }

    @Test
    void ratingAllowsZeroStars() {
        Rating r = new Rating();
        setPrivateField(r, "rating", 0);

        assertEquals(0, r.getRating());
    }

    @Test
    void ratingAllowsMaxStars() {
        Rating r = new Rating();
        setPrivateField(r, "rating", 5);

        assertEquals(5, r.getRating());
    }

    @Test
    void ratingDefaultsToZero() {
        Rating r = new Rating();
        assertEquals(0, r.getRating());
    }

    // ---------- helper ----------
    private static void setPrivateField(Object target, String field, Object value) {
        try {
            var f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            fail(e);
        }
    }
}
