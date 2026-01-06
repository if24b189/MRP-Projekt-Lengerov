package org.example.server.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Request {

    private final HttpExchange exchange;
    private Integer userId;

    public Request(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public String getPath() {
        String path = exchange.getRequestURI().getPath();

        // trailing slash entfernen
        if (path.endsWith("/") && path.length() > 1) {
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }

    public String getMethod() {
        return exchange.getRequestMethod();
    }

    public String getHeader(String name) {
        return exchange.getRequestHeaders().getFirst(name);
    }

    public String getBody() {
        try (InputStream is = exchange.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    public int getPathId() {
        String[] parts = getPath().split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
