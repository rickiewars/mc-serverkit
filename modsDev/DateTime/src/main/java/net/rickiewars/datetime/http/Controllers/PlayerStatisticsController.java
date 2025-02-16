package net.rickiewars.datetime.http.Controllers;

import com.sun.net.httpserver.HttpExchange;
import net.minecraft.server.MinecraftServer;
import net.rickiewars.datetime.DateTime;
import net.rickiewars.datetime.http.Services.StatisticsService;
import net.rickiewars.datetime.module.http.HttpStatus;
import net.rickiewars.datetime.module.http.JsonResponse;

@SuppressWarnings("unused")
public class PlayerStatisticsController {
    private final StatisticsService statisticsService;

    public PlayerStatisticsController() {
        this(DateTime.getServer());
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
