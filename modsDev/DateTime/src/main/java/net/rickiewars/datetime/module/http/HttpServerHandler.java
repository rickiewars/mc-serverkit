package net.rickiewars.datetime.module.http;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import net.rickiewars.datetime.DateTime;
import net.rickiewars.datetime.http.Routes;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

public class HttpServerHandler {
    private static HttpServerHandler instance;
    private HttpServer server;

    public static HttpServerHandler getInstance() {
        if (instance == null) {
            instance = new HttpServerHandler();
        }
        return instance;
    }

    public static HttpServer getServer() {
        return getInstance().server;
    }

    private HttpServerHandler() {
    }

    public void startServer(String host, int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(host, port), 0);
        Routes.define();
        server.setExecutor(null); // creates a default executor
        server.start();
        DateTime.LOGGER.info("HTTP Server started on " + host + ":" + port);
    }

    public void stopServer() {
        if (server != null) {
            server.stop(10);
        }
    }
}
