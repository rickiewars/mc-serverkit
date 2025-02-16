package net.rickiewars.datetime.http.Resources;

import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ServerScoreboard;
import net.rickiewars.datetime.http.Models.Objective;
import net.rickiewars.datetime.http.Models.Score;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class StatisticsResource {
    public static Score toScore(ScoreboardEntry score) {
        return new Score(score.name().getString(), score.value());
    }

    public static Objective toObjective(@NotNull ScoreboardObjective objective, ServerScoreboard scoreboard) {
        List<Score> scores = scoreboard.getScoreboardEntries(objective).stream()
            .map(StatisticsResource::toScore)
            .collect(Collectors.toList());

        return new Objective(
            objective.getName(),
            objective.getCriterion().getName(),
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
