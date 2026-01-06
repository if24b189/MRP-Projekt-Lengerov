package org.example.server.http;

import com.sun.net.httpserver.HttpExchange;
import org.example.util.JsonUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Response {

    private final HttpExchange exchange;
    private Status status = Status.OK;
    private String body = "";

    public Response(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setJson(Object obj) {
        this.body = JsonUtil.toJson(obj);
        exchange.getResponseHeaders().set(
                "Content-Type",
                ContentType.JSON.getValue()
        );
    }

    public void send() throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status.getCode(), bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
