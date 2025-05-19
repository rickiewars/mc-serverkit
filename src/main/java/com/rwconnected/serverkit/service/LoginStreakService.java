package com.rwconnected.serverkit.service;

import com.rwconnected.serverkit.api.economy.EconomyProvider;
import com.rwconnected.serverkit.api.economy.IAccount;
import com.rwconnected.serverkit.api.economy.ITransaction;
import com.rwconnected.serverkit.api.minecraft.player.IPlayer;
import com.rwconnected.serverkit.api.minecraft.storage.virtual.IStorage;
import com.rwconnected.serverkit.config.Config;
import com.rwconnected.serverkit.module.Log;
import com.rwconnected.serverkit.api.util.time.ITimeProvider;
import com.rwconnected.serverkit.util.ModUtils;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

public class LoginStreakService {

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private final ITimeProvider timeProvider;
    private final EconomyProvider economyProvider;

    private enum StreakResult {
        NO_CHANGE,
        STREAK_MAINTAINED,
        STREAK_LOST,
        NEW_PLAYER,
        ERROR
    }

    public LoginStreakService(@NotNull ITimeProvider timeProvider, EconomyProvider economyProvider) {
        this.timeProvider = timeProvider;
        this.economyProvider = economyProvider;
    }

    public void process(IPlayer<?> player) {
        int currentStreak = getStreak(player);
        int currentRecord = getRecord(player);
        StreakResult result = processStreak(player);
        processFeedback(player, result, currentStreak, currentRecord);
    }

    public int getStreak(IPlayer<?> player) {
        return player.getLoginStreakStorage().get();
    }

    public int getRecord(IPlayer<?> player) {
        return player.getLoginStreakRecordStorage().get();
    }

    public int setStreak(IPlayer<?> player, int streak) {
        if (streak < 0) {
            streak = 0;
        }
        player.getLoginStreakStorage().set(streak);
        updateRecord(player);
        return streak;
    }

    public int setRecord(IPlayer<?> player, int record) {
        if (record < 0) {
            record = 0;
        }
        int currentStreak = getStreak(player);
        if (record < currentStreak) {
            Log.warning("LoginStreakService::setRecord() -> The record cannot be less than the current streak, setting the current streak equal to the record.");
            player.getLoginStreakStorage().set(record);
        }
        player.getLoginStreakRecordStorage().set(record);
        return record;
    }

    public int reward(IPlayer<?> player, int amount) {
        amount = Math.max(0, amount);

        Identifier currencyId = Config.instance().economy.currencyId();
        try {
            IAccount account = economyProvider.getDefaultAccount(player, currencyId);
            ITransaction transaction = account.increaseBalance(amount);
            if (transaction.isFailure()) {
                Log.error("LoginStreakService::reward() -> Failed to reward player " + player.getName() + " with " + ModUtils.formatCurrency(amount));
                return 0;
            }
        } catch (IllegalArgumentException e) {
            Log.error("LoginStreakService::reward() -> Couldn't interact with economy provider: " + e.getMessage());
            return 0;
        }

        return amount;
    }

    private StreakResult processStreak(IPlayer<?> player) {
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
        updateRecord(player);
        return result;
    }

    private void updateRecord(IPlayer<?> player) {
        int currentRecord = getRecord(player);
        int streak = getStreak(player);
        if (streak > currentRecord) {
            player.getLoginStreakRecordStorage().set(streak);
        }
    }

    private void processFeedback(IPlayer<?> player, StreakResult result, int previousStreak, int previousRecord) {
        if (player == null) {
            Log.error("StreakResult::processFeedback() -> Player not found");
            return;
        }

        switch (result) {
            case STREAK_MAINTAINED:
                final int newStreak = previousStreak + 1;
                final int newRecord = Math.max(newStreak, previousRecord);
                Config.instance().loginStreak.milestones().forEach(milestone -> {
                    if (milestone.periodic()) {
                        if (newStreak % milestone.days() == 0) {
                            player.sendMessage(milestone.parseMessage(newStreak));
                            reward(player, milestone.reward());
                        }
                    } else {
                        if (newRecord != previousRecord && // Only process if record changed
                            previousRecord < milestone.days() && // Allow for corrections
                            newRecord >= milestone.days() // Check if milestone is reached
                        ) {
                            player.sendMessage(milestone.parseMessage(newRecord));
                            reward(player, milestone.reward());
                        }
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
