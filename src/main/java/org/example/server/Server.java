package org.example.server;

import com.sun.net.httpserver.HttpServer;
import org.example.handler.*;
import org.example.server.auth.AuthMiddleware;
import org.example.server.http.*;

import java.net.InetSocketAddress;

public class Server {

    public void start() throws Exception {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        Router router = new Router();
        router.register("/users", new UserHandler());
        router.register("/media", new MediaHandler());
        router.register("/ratings", new RatingHandler());
        router.register("/favorites", new FavoriteHandler());

        httpServer.createContext("/", exchange -> {
            Request request = new Request(exchange);
            Response response = new Response(exchange);

            Integer userId = AuthMiddleware.authenticate(request);
            request.setUserId(userId);

            router.route(request, response);
            response.send();
        });

        httpServer.start();
        System.out.println("Server running on http://localhost:8080");
    }
}
