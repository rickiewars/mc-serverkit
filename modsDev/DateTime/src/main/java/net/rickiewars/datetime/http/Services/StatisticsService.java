package net.rickiewars.datetime.http.Services;

import com.google.gson.Gson;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.rickiewars.datetime.http.Models.Objective;
import net.rickiewars.datetime.http.Resources.StatisticsResource;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

public class StatisticsService {
    private final MinecraftServer server;

    public StatisticsService(MinecraftServer server) {
        this.server = server;
    }

    public CompletableFuture<List<Objective>> collectPlayerStatistics() {
        CompletableFuture<List<Objective>> future = new CompletableFuture<>();
        this.server.execute(() -> {
            ServerScoreboard scoreboard = server.getScoreboard();
            future.complete(StatisticsResource.toObjectiveList(scoreboard));
        });
        return future;
    }

    public CompletableFuture<Objective> collectPlayerStatistics(String objectiveName) {
        CompletableFuture<Objective> future = new CompletableFuture<>();
        this.server.execute(() -> {
            ServerScoreboard scoreboard = server.getScoreboard();
            ScoreboardObjective objective = scoreboard.getObjectives().stream()
                .filter(obj -> obj.getName().equals(objectiveName))
                .findFirst().orElse(null);
            if (objective == null) {
                future.completeExceptionally(new NoSuchElementException("Objective not found"));
                return;
            }
            future.complete(StatisticsResource.toObjective(objective, scoreboard));
        });
        return future;
    }
}
