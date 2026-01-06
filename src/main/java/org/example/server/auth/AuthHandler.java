package org.example.server.auth;

import org.example.handler.Handler;
import org.example.models.User;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;
import org.example.util.JsonUtil;

import java.sql.SQLException;
import java.util.Map;

public class AuthHandler implements Handler {

    @Override
    public void handle(Request req, Response res) {
        String method = req.getMethod();
        String path = req.getPath();

        try {
            if ("POST".equalsIgnoreCase(method) && path.endsWith("/login")) {
                handleLogin(req, res);
                return;
            }
            if ("POST".equalsIgnoreCase(method) && path.endsWith("/register")) {
                handleRegister(req, res);
                return;
            }
            if ("POST".equalsIgnoreCase(method) && path.endsWith("/logout")) {
                handleLogout(req, res);
                return;
            }
            if ("GET".equalsIgnoreCase(method) && path.endsWith("/me")) {
                handleMe(req, res);
                return;
            }

            res.setJson(Map.of("error", "Not found"));
            res.setStatus(Status.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            res.setJson(Map.of("error", e.getMessage()));
            res.setStatus(Status.BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
            res.setJson(Map.of("error", "Database error"));
            res.setStatus(Status.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            res.setJson(Map.of("error", "Internal server error"));
            res.setStatus(Status.INTERNAL_SERVER_ERROR);
        }
    }

    private void handleLogin(Request req, Response res) throws Exception {
        Map<String, Object> body = parseRequestBody(req);
        String username = getString(body, "username");
        String password = getString(body, "password");
        if (username == null || password == null) {
            res.setJson(Map.of("error", "username and password required"));
            res.setStatus(Status.BAD_REQUEST);
            return;
        }
        String token = AuthService.login(username, password);
        res.setJson(Map.of("token", token));
        res.setStatus(Status.OK);
    }

    private void handleRegister(Request req, Response res) throws Exception {
        Map<String, Object> body = parseRequestBody(req);
        String username = getString(body, "username");
        String password = getString(body, "password");
        if (username == null || password == null) {
            res.setJson(Map.of("error", "username and password required"));
            res.setStatus(Status.BAD_REQUEST);
            return;
        }
        boolean success = AuthService.register(username, password);
        if (success) {
            res.setJson(Map.of("message", "User registered successfully"));
            res.setStatus(Status.CREATED);
        } else {
            res.setJson(Map.of("error", "Registration failed"));
            res.setStatus(Status.BAD_REQUEST);
        }
    }

    private void handleLogout(Request req, Response res) {
        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            res.setJson(Map.of("error", "Token required in Authorization header"));
            res.setStatus(Status.BAD_REQUEST);
            return;
        }
        AuthService.logout(auth.substring("Bearer ".length()));
        res.setJson(Map.of("message", "Logged out successfully"));
        res.setStatus(Status.OK);
    }

    private void handleMe(Request req, Response res) {
        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            res.setJson(Map.of("error", "Authorization header required"));
            res.setStatus(Status.UNAUTHORIZED);
            return;
        }
        String token = auth.substring("Bearer ".length());
        User user = AuthService.validateToken(token);
        if (user == null) {
            res.setJson(Map.of("error", "Invalid token"));
            res.setStatus(Status.UNAUTHORIZED);
            return;
        }
        res.setJson(Map.of("id", user.getId(), "username", user.getUsername()));
        res.setStatus(Status.OK);
    }

    private Map<String, Object> parseRequestBody(Request req) {
        String body = req.getBody();
        if (body == null || body.isBlank()) return Map.of();
        return JsonUtil.fromJson(body, Map.class);
    }

    private String getString(Map<String, Object> m, String key) {
        return m.get(key) == null ? null : m.get(key).toString();
    }
}
