package org.example.handler;

import org.example.dto.UserProfileResponse;
import org.example.models.User;
import org.example.services.UserService;
import org.example.server.auth.AuthService;
import org.example.server.http.*;
import org.example.util.JsonUtil;

import java.util.Map;

public class UserHandler implements Handler {

    private final UserService service = new UserService();

    @Override
    public void handle(Request req, Response res) {

        try {
            String path = req.getPath();
            String method = req.getMethod();
            String body = req.getBody();

            // ---------- REGISTER ----------
            if ("POST".equalsIgnoreCase(method) && path.endsWith("/register")) {

                if (body == null || body.isBlank()) {
                    res.setStatus(Status.BAD_REQUEST);
                    res.setJson(Map.of("error", "request body required"));
                    return;
                }

                User user = JsonUtil.fromJson(body, User.class);

                if (user.getUsername() == null || user.getPassword() == null) {
                    res.setStatus(Status.BAD_REQUEST);
                    res.setJson(Map.of("error", "username and password required"));
                    return;
                }

                service.register(user);
                res.setStatus(Status.CREATED);
                res.setJson(Map.of("message", "User registered successfully"));
                return;
            }

            // ---------- LOGIN ----------
            if ("POST".equalsIgnoreCase(method) && path.endsWith("/login")) {

                if (body == null || body.isBlank()) {
                    res.setStatus(Status.BAD_REQUEST);
                    res.setJson(Map.of("error", "request body required"));
                    return;
                }

                User user = JsonUtil.fromJson(body, User.class);
                int userId = service.login(user);

                String token = AuthService.createToken(userId);
                res.setStatus(Status.OK);
                res.setJson(Map.of("token", token));
                return;
            }

            // ---------- PROFILE ----------
            if ("GET".equalsIgnoreCase(method) && path.endsWith("/profile")) {

                Integer userId = req.getUserId();
                if (userId == null) {
                    res.setStatus(Status.UNAUTHORIZED);
                    return;
                }

                UserProfileResponse profile = service.getProfile(userId);
                res.setStatus(Status.OK);
                res.setJson(profile);
                return;
            }

            res.setStatus(Status.NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(Status.BAD_REQUEST);
            res.setJson(Map.of("error", e.getMessage()));
        }
    }
}
