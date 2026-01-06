package org.example.server;

import org.example.handler.Handler;
import org.example.server.auth.AuthMiddleware;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Router {

    private final Map<String, Handler> routes = new ConcurrentHashMap<>();

    public void register(String basePath, Handler handler) {
        String normalized = normalizeBase(basePath);
        routes.put(normalized, handler);
    }

    public void route(Request req, Response res) throws IOException {
        String path = normalizePath(req.getPath());
        String bestMatch = null;
        int bestLen = -1;

        Set<String> keys = routes.keySet();
        for (String base : keys) {
            if (path.startsWith(base) && base.length() > bestLen) {
                bestMatch = base;
                bestLen = base.length();
            }
        }

        if (bestMatch != null) {

            // ===== AUTHENTICATION =====
            if (!path.equals("/users/login") && !path.equals("/users/register")) {
                Integer userId = AuthMiddleware.authenticate(req);
                if (userId == null) {
                    res.setStatus(Status.UNAUTHORIZED);
                    return;
                }
                req.setUserId(userId);
            }

            Handler h = routes.get(bestMatch);
            h.handle(req, res);

        } else {
            res.setStatus(Status.NOT_FOUND);
        }
    }

    private String normalizeBase(String base) {
        if (base == null || base.isEmpty()) return "/";
        String p = base.startsWith("/") ? base : "/" + base;
        if (p.length() > 1 && p.endsWith("/")) p = p.substring(0, p.length() - 1);
        return p;
    }

    private String normalizePath(String path) {
        if (path == null || path.isEmpty()) return "/";
        return path.startsWith("/") ? path : "/" + path;
    }
}
