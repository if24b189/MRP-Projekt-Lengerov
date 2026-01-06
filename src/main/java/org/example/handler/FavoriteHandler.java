package org.example.handler;

import org.example.dto.FavoriteCreateRequest;
import org.example.services.FavoriteService;
import org.example.server.http.*;
import org.example.util.JsonUtil;

import java.io.IOException;

public class FavoriteHandler implements Handler {

    private final FavoriteService service = new FavoriteService();

    @Override
    public void handle(Request req, Response res) throws IOException {

        Integer userId = req.getUserId();
        if (userId == null) {
            res.setStatus(Status.UNAUTHORIZED);
            return;
        }

        // ---------- ADD FAVORITE ----------
        if ("POST".equals(req.getMethod()) && "/favorites".equals(req.getPath())) {
            try {
                FavoriteCreateRequest dto =
                        JsonUtil.fromJson(req.getBody(), FavoriteCreateRequest.class);

                service.add(userId, dto.mediaId);
                res.setStatus(Status.CREATED);

            } catch (Exception e) {
                e.printStackTrace();
                res.setStatus(Status.BAD_REQUEST);
            }
            return;
        }

        // ---------- GET FAVORITES ----------
        if ("GET".equals(req.getMethod()) && "/favorites".equals(req.getPath())) {
            res.setJson(service.getAll(userId));
            res.setStatus(Status.OK);
            return;
        }

        // ---------- REMOVE FAVORITE ----------
        if ("DELETE".equals(req.getMethod()) && req.getPath().startsWith("/favorites/")) {
            int mediaId = req.getPathId();
            service.remove(userId, mediaId);
            res.setStatus(Status.OK);
            return;
        }

        res.setStatus(Status.NOT_FOUND);
    }
}
