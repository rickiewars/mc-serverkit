package com.rwconnected.serverkit.http;

import com.rwconnected.serverkit.http.Controllers.HealthController;
import com.rwconnected.serverkit.http.Controllers.PlayerStatisticsController;
import com.rwconnected.serverkit.module.http.Route;

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
