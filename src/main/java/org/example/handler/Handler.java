package org.example.handler;

import org.example.server.http.Request;
import org.example.server.http.Response;

import java.io.IOException;

public interface Handler {
    void handle(Request request, Response response) throws IOException;
}
