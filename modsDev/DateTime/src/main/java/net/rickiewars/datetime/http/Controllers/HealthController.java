package net.rickiewars.datetime.http.Controllers;

import com.sun.net.httpserver.HttpExchange;
import net.minecraft.server.MinecraftServer;
import net.rickiewars.datetime.DateTime;
import net.rickiewars.datetime.module.http.HttpStatus;
import net.rickiewars.datetime.module.http.JsonResponse;

import java.io.IOException;

@SuppressWarnings("unused")
public class HealthController {
    public void healthCheck(HttpExchange exchange) throws IOException {
        MinecraftServer server = DateTime.getServer();
        if (server == null) {
            exchange.sendResponseHeaders(HttpStatus.SERVICE_UNAVAILABLE.code(), -1);
            return;
        }

        JsonResponse.send(exchange, HttpStatus.OK, "OK");
    }
}
