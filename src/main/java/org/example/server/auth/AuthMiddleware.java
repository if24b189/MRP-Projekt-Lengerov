package org.example.server.auth;

import org.example.server.http.Request;

public class AuthMiddleware {

    public static Integer authenticate(Request request) {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            return null;
        }
        return AuthService.getUserId(auth.substring(7));
    }
}
