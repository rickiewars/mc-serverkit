package com.rwconnected.serverkit.http.Resources;

import com.rwconnected.serverkit.http.Models.Objective;
import com.rwconnected.serverkit.http.Models.Score;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.world.scores.PlayerScoreEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class StatisticsResource {
    public static Score toScore(PlayerScoreEntry score) {
        return new Score(score.owner(), score.value());
    }

    public static Objective toObjective(@NotNull net.minecraft.world.scores.Objective objective, ServerScoreboard scoreboard) {
        List<Score> scores = scoreboard.listPlayerScores(objective).stream()
            .map(StatisticsResource::toScore)
            .collect(Collectors.toList());

        return new Objective(
            objective.getName(),
            objective.getCriteria().getName(),
            objective.getDisplayName().getString(),
            scores
        );
    }

    public static List<Objective> toObjectiveList(ServerScoreboard scoreboard) {
        return scoreboard.getObjectives().stream()
            .map(obj -> toObjective(obj, scoreboard))
            .collect(Collectors.toList());
    }
}
