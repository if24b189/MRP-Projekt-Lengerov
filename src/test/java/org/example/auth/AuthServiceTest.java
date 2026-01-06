package org.example.auth;

import org.example.server.auth.AuthService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Test
    void createTokenReturnsNonNull() {
        String token = AuthService.createToken(1);
        assertNotNull(token);
    }

    @Test
    void tokenResolvesCorrectUserId() {
        String token = AuthService.createToken(42);
        assertEquals(42, AuthService.getUserId(token));
    }

    @Test
    void logoutRemovesToken() {
        String token = AuthService.createToken(5);
        AuthService.logout(token);
        assertNull(AuthService.getUserId(token));
    }

    @Test
    void validateTokenWithInvalidTokenReturnsNull() {
        assertNull(AuthService.validateToken("invalid-token"));
    }

    @Test
    void tokensAreUnique() {
        String t1 = AuthService.createToken(1);
        String t2 = AuthService.createToken(1);
        assertNotEquals(t1, t2);
    }

    @Test
    void multipleTokensWorkIndependently() {
        String t1 = AuthService.createToken(1);
        String t2 = AuthService.createToken(2);

        assertEquals(1, AuthService.getUserId(t1));
        assertEquals(2, AuthService.getUserId(t2));
    }
}
