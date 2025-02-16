package net.rickiewars.datetime.http;

import net.rickiewars.datetime.http.Controllers.HealthController;
import net.rickiewars.datetime.http.Controllers.PlayerStatisticsController;
import net.rickiewars.datetime.module.http.Route;

public class Routes {

    public static void define() {
        Route.define((root) -> {
            root.group("/api", (api) -> {
                api.apiResource("/player-statistics", PlayerStatisticsController.class)
                    .only("index", "show");
                api.get("/online-players", PlayerStatisticsController.class, "index");
            });
            root.get("/health-check", HealthController.class, "healthCheck");
        });
    }
}
