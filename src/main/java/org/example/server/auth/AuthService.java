package org.example.server.auth;

import org.example.databases.Database;
import org.example.models.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthService {

    private static final Map<String, Integer> activeTokens = new HashMap<>();

    // =====================
    // CREATE TOKEN
    // =====================
    public static String createToken(int userId) {
        String token = UUID.randomUUID().toString();
        activeTokens.put(token, userId);
        return token;
    }

    // =====================
    // LOGIN
    // =====================
    public static String login(String username, String password) throws SQLException {
        int userId = Database.login(username, password);
        return createToken(userId);
    }

    // =====================
    // REGISTER ✅ FIX
    // =====================
    public static boolean register(String username, String password) throws SQLException {
        Database.createUser(username, password);
        return true;
    }

    // =====================
    // LOGOUT
    // =====================
    public static void logout(String token) {
        activeTokens.remove(token);
    }

    // =====================
    // TOKEN VALIDATION
    // =====================
    public static User validateToken(String token) {
        Integer userId = activeTokens.get(token);
        if (userId == null) return null;
        return Database.getUser(userId);
    }

    // =====================
    // USER ID FROM TOKEN
    // =====================
    public static Integer getUserId(String token) {
        return activeTokens.get(token);
    }
}
