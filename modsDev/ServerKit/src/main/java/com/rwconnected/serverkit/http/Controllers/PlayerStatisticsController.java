package com.rwconnected.serverkit.http.Controllers;

import com.rwconnected.serverkit.ServerKit;
import com.rwconnected.serverkit.http.Services.StatisticsService;
import com.sun.net.httpserver.HttpExchange;
import net.minecraft.server.MinecraftServer;
import com.rwconnected.serverkit.module.http.HttpStatus;
import com.rwconnected.serverkit.module.http.JsonResponse;

@SuppressWarnings("unused")
public class PlayerStatisticsController {
    private final StatisticsService statisticsService;

    public PlayerStatisticsController() {
        this(ServerKit.getServer());
    }

    public PlayerStatisticsController(MinecraftServer server) {
        this.statisticsService = new StatisticsService(server);
    }
    public void index(HttpExchange exchange) {
        statisticsService.collectPlayerStatistics()
            .thenAccept(data -> JsonResponse.send(exchange, HttpStatus.OK, data))
            .exceptionally(e -> {
                JsonResponse.sendError(exchange, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                return null;
            });
    }

    public void show(HttpExchange exchange, String objectiveName) {
        statisticsService.collectPlayerStatistics(objectiveName)
            .thenAccept(data -> JsonResponse.send(exchange, HttpStatus.OK, data))
            .exceptionally(e -> {
                JsonResponse.sendError(exchange, HttpStatus.NOT_FOUND, "Objective not found");
                return null;
            });
    }

}
