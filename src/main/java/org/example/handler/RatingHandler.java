package org.example.handler;

import org.example.dto.RatingCreateRequest;
import org.example.models.Rating;
import org.example.services.RatingService;
import org.example.server.http.*;
import org.example.util.JsonUtil;

import java.io.IOException;

public class RatingHandler implements Handler {

    private final RatingService service = new RatingService();

    @Override
    public void handle(Request req, Response res) throws IOException {

        // ---------- CREATE ----------
        if ("POST".equals(req.getMethod()) && "/ratings".equals(req.getPath())) {

            Integer userId = req.getUserId();
            if (userId == null) {
                res.setStatus(Status.UNAUTHORIZED);
                return;
            }

            RatingCreateRequest dto =
                    JsonUtil.fromJson(req.getBody(), RatingCreateRequest.class);

            Rating rating = new Rating();
            rating.setMediaId(dto.mediaId);
            rating.setRating(dto.rating);
            rating.setComment(dto.comment);

            service.rate(rating, userId);
            res.setStatus(Status.CREATED);
            return;
        }

        // ---------- GET RATINGS FOR MEDIA ----------
        if ("GET".equals(req.getMethod()) && req.getPath().startsWith("/ratings/media/")) {
            int mediaId = req.getPathId();
            res.setJson(service.getByMedia(mediaId));
            res.setStatus(Status.OK);
            return;
        }

        // ---------- GET RATINGS BY USER ----------
        if ("GET".equals(req.getMethod()) && "/ratings/user".equals(req.getPath())) {

            Integer userId = req.getUserId();
            if (userId == null) {
                res.setStatus(Status.UNAUTHORIZED);
                return;
            }

            res.setJson(service.getRatingsByUser(userId));
            res.setStatus(Status.OK);
            return;
        }

        // ---------- DELETE ----------
        if ("DELETE".equals(req.getMethod()) && req.getPath().startsWith("/ratings/")) {

            Integer userId = req.getUserId();
            if (userId == null) {
                res.setStatus(Status.UNAUTHORIZED);
                return;
            }

            int id = req.getPathId();
            service.delete(id, userId);
            res.setStatus(Status.OK);
            return;
        }

        res.setStatus(Status.NOT_FOUND);
    }
}
