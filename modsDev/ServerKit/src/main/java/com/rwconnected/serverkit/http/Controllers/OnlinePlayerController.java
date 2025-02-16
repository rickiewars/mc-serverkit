package com.rwconnected.serverkit.http.Controllers;

import com.sun.net.httpserver.HttpExchange;
import com.rwconnected.serverkit.ServerKit;
import com.rwconnected.serverkit.http.Services.PlayerService;
import com.rwconnected.serverkit.module.http.HttpStatus;
import com.rwconnected.serverkit.module.http.JsonResponse;

import java.io.IOException;

@SuppressWarnings("unused")
public class OnlinePlayerController {
    private final PlayerService playerService;

    public OnlinePlayerController() {
        this.playerService = new PlayerService(ServerKit.getServer());
    }

    public void index(HttpExchange exchange) throws IOException {
        this.playerService.listOnlinePlayers()
            .thenAccept(data -> JsonResponse.send(exchange, HttpStatus.OK, data))
            .exceptionally(e -> {
                JsonResponse.sendError(exchange, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                return null;
            });
    }
}
