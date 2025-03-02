package com.rwconnected.serverkit.http.Controllers;

import com.sun.net.httpserver.HttpExchange;
import net.minecraft.server.MinecraftServer;
import com.rwconnected.serverkit.ServerKit;
import com.rwconnected.serverkit.module.http.HttpStatus;
import com.rwconnected.serverkit.module.http.JsonResponse;

import java.io.IOException;

@SuppressWarnings("unused")
public class HealthController {
    public void healthCheck(HttpExchange exchange) throws IOException {
        MinecraftServer server = ServerKit.getServer();
        if (server == null) {
            exchange.sendResponseHeaders(HttpStatus.SERVICE_UNAVAILABLE.code(), -1);
            return;
        }

        JsonResponse.send(exchange, HttpStatus.OK, "OK");
    }
}
