package net.rickiewars.datetime.http.Controllers;

import com.sun.net.httpserver.HttpExchange;
import net.rickiewars.datetime.DateTime;
import net.rickiewars.datetime.http.Services.PlayerService;
import net.rickiewars.datetime.module.http.HttpStatus;
import net.rickiewars.datetime.module.http.JsonResponse;

import java.io.IOException;

@SuppressWarnings("unused")
public class OnlinePlayerController {
    private final PlayerService playerService;

    public OnlinePlayerController() {
        this.playerService = new PlayerService(DateTime.getServer());
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
