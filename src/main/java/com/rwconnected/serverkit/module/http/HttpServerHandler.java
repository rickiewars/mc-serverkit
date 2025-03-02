package com.rwconnected.serverkit.module.http;

import com.rwconnected.serverkit.ServerKit;
import com.rwconnected.serverkit.http.Routes;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

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
        ServerKit.LOGGER.info("HTTP Server started on " + host + ":" + port);
    }

    public void stopServer() {
        if (server != null) {
            server.stop(10);
        }
    }
}
