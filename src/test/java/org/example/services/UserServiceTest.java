package org.example.services;

import org.example.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Test
    void userStoresIdCorrectly() {
        User user = new User(10, "alice");
        assertEquals(10, user.getId());
    }

    @Test
    void userStoresUsernameCorrectly() {
        User user = new User(1, "bob");
        assertEquals("bob", user.getUsername());
    }

    @Test
    void passwordCanBeNull() {
        User user = new User(1, "charlie");
        assertNull(user.getPassword());
    }

    @Test
    void toStringContainsUsername() {
        User user = new User(1, "david");
        assertTrue(user.toString().contains("david"));
    }
}
