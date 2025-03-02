package com.rwconnected.serverkit.service;

import com.rwconnected.serverkit.api.minecraft.player.IPlayer;
import com.rwconnected.serverkit.api.minecraft.storage.virtual.IStorage;
import com.rwconnected.serverkit.config.Config;
import com.rwconnected.serverkit.module.Log;
import com.rwconnected.serverkit.api.util.time.ITimeProvider;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

public class LoginStreakService {

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private final ITimeProvider timeProvider;

    private enum StreakResult {
        NO_CHANGE,
        STREAK_MAINTAINED,
        STREAK_LOST,
        NEW_PLAYER,
        ERROR
    }

    public LoginStreakService(@NotNull ITimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public void process(IPlayer player) {
        int currentStreak = player.getLoginStreakStorage().get();
        StreakResult result = processStreak(player);
        processFeedback(player, result, currentStreak);
    }

    private StreakResult processStreak(IPlayer player) {
        StreakResult result = StreakResult.NO_CHANGE;
        IStorage<String> lastLoginDateStore = player.getLastLoginDateStorage();
        String lastLoginDate = lastLoginDateStore.get();
        String today = today();
        if (today.equals(lastLoginDate)) {
            return result;
        }

        if (yesterday().equals(lastLoginDate)) {
            result = StreakResult.STREAK_MAINTAINED;
            player.getLoginStreakStorage().increment();
        } else {
            result = lastLoginDate.isEmpty()
                    ? StreakResult.NEW_PLAYER
                    : StreakResult.STREAK_LOST;
            player.getLoginStreakStorage().reset();
        }

        lastLoginDateStore.set(today);
        return result;
    }

    private void processFeedback(IPlayer player, StreakResult result, int previousStreak) {
        if (player == null) {
            Log.error("StreakResult::processFeedback() -> Player not found");
            return;
        }

        switch (result) {
            case STREAK_MAINTAINED:
                int newStreak = previousStreak + 1;
                Config.instance().loginStreak.milestones().forEach(milestone -> {
                    if (milestone.days() == newStreak || (
                        milestone.periodic() && newStreak % milestone.days() == 0
                    )) {
                        player.sendMessage(milestone.parseMessage(newStreak));
                    }
                });
                break;
            case STREAK_LOST:
                if (previousStreak > 1) {
                    player.sendMessage(
                        Config.instance().loginStreak.parseStreakLostMessage(previousStreak)
                    );
                }
                break;
            case NEW_PLAYER:
                player.sendMessage(Config.instance().loginStreak.welcomeMessage());
                break;
        }
    }

    private String today() {
        return dateFormatter.format(this.timeProvider.now());
    }

    private String yesterday() {
        return dateFormatter.format(this.timeProvider.yesterday());
    }
}
