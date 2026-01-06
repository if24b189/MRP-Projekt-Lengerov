package org.example.handler;

import org.example.dto.MediaCreateRequest;
import org.example.models.Media;
import org.example.services.MediaService;
import org.example.server.http.*;
import org.example.util.JsonUtil;

import java.io.IOException;

public class MediaHandler implements Handler {

    private final MediaService service = new MediaService();

    @Override
    public void handle(Request req, Response res) throws IOException {

        if ("POST".equals(req.getMethod()) && "/media".equals(req.getPath())) {
            try {
                Integer userId = req.getUserId();
                if (userId == null) {
                    res.setStatus(Status.UNAUTHORIZED);
                    return;
                }

                MediaCreateRequest dto =
                        JsonUtil.fromJson(req.getBody(), MediaCreateRequest.class);

                Media media = new Media(
                        0,
                        dto.title,
                        dto.description,
                        dto.genre,
                        dto.type,
                        dto.ageRestriction,
                        userId
                );

                service.create(media, userId);
                res.setStatus(Status.CREATED);

            } catch (Exception e) {
                e.printStackTrace(); // 🔴 fürs Debuggen
                res.setStatus(Status.BAD_REQUEST);
            }
            return;
        }

        if ("GET".equals(req.getMethod()) && "/media".equals(req.getPath())) {
            res.setJson(service.getAll());
            res.setStatus(Status.OK);
            return;
        }

        if ("PUT".equals(req.getMethod()) && req.getPath().startsWith("/media/")) {
            try {
                Integer userId = req.getUserId();
                if (userId == null) {
                    res.setStatus(Status.UNAUTHORIZED);
                    return;
                }

                int id = req.getPathId();
                MediaCreateRequest dto =
                        JsonUtil.fromJson(req.getBody(), MediaCreateRequest.class);

                Media media = new Media(
                        id,
                        dto.title,
                        dto.description,
                        dto.genre,
                        dto.type,
                        dto.ageRestriction,
                        userId
                );

                service.update(id, media, userId);
                res.setStatus(Status.OK);

            } catch (Exception e) {
                e.printStackTrace();
                res.setStatus(Status.BAD_REQUEST);
            }
            return;
        }

        if ("DELETE".equals(req.getMethod()) && req.getPath().startsWith("/media/")) {
            try {
                Integer userId = req.getUserId();
                if (userId == null) {
                    res.setStatus(Status.UNAUTHORIZED);
                    return;
                }

                int id = req.getPathId();
                service.delete(id, userId);
                res.setStatus(Status.OK);

            } catch (Exception e) {
                e.printStackTrace();
                res.setStatus(Status.BAD_REQUEST);
            }
            return;
        }

        res.setStatus(Status.NOT_FOUND);
    }
}
