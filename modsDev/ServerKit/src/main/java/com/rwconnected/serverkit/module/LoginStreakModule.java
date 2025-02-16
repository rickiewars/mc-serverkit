package com.rwconnected.serverkit.module;

import com.rwconnected.serverkit.storage.LastLoginDate;
import com.rwconnected.serverkit.storage.LoginStreak;
import com.rwconnected.serverkit.util.IEntityDataSaver;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoginStreakModule {
    private static final String HELP_MSG =
            "In this server, you can maintain a login streak by logging in every day.\n" +
                    "If you miss a day, your streak will reset to 1.\n" +
                    "You can check your login streak with the command /loginStreak";

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private enum StreakResult {
        NO_CHANGE,
        STREAK_MAINTAINED,
        STREAK_LOST,
        NEW_PLAYER,
        ERROR
    }

    public static String getHelpMessage() {
        return HELP_MSG;
    }

    public static int process(ServerPlayerEntity player) {
        if (player == null) {
            Log.error("StreakResult::process() -> Player not found");
            return 0;
        }
        Log.debug("StreakResult::process(" + player.getName().getString() + ") -> Processing...");

        int currentStreak = LoginStreak.get((IEntityDataSaver) player);

        switch (process((IEntityDataSaver) player)) {
            case STREAK_MAINTAINED:
                int newStreak = LoginStreak.get((IEntityDataSaver) player);
                Log.info("Player " + player.getName().getString() + " now has a login streak of " + newStreak + " days.");
                player.sendMessage(Text.of("Congratulations! You've been able to maintain your login streak!"), false);
                player.sendMessage(Text.of(
                        "You've logged in " + newStreak + " days in a row now!"
                ), false);
                break;
            case STREAK_LOST:
                if (currentStreak > 1) {
                    Log.info("Player " + player.getName().getString() + " has lost their login streak of " + currentStreak + " days.");
                }
                player.sendMessage(Text.of("Your login streak of " + currentStreak + " days has been lost"), false);
                break;
            case NEW_PLAYER:
                player.sendMessage(Text.of(HELP_MSG), false);
                break;
        }
        return 1;
    }

    private static StreakResult process(IEntityDataSaver player) {
        if (player == null) {
            Log.error("StreakResult::process() -> Player not found");
            return StreakResult.ERROR;
        }
        StreakResult result = StreakResult.NO_CHANGE;

        Calendar cal = Calendar.getInstance();

        String currentDate = dateFormatter.format(cal.getTime());
        String lastLoginDate = LastLoginDate.get(player);

        Log.debug("Current date: " + currentDate);
        Log.debug("Last login date: " + lastLoginDate);

        if (!currentDate.equals(lastLoginDate)) {
            // Get yesterday's date
            cal.add(Calendar.DATE, -1);
            String yesterday = dateFormatter.format(cal.getTime());

            Log.debug("Yesterday's date: " + yesterday);

            if (yesterday.equals(lastLoginDate)) {
                // Increment the login streak
                LoginStreak.increment(player);
                result = StreakResult.STREAK_MAINTAINED;
            } else {
                result = lastLoginDate.isEmpty()
                        ? StreakResult.NEW_PLAYER
                        : StreakResult.STREAK_LOST;
                // Reset the login streak
                LoginStreak.reset(player);
            }

            LastLoginDate.set(player, currentDate);
        }
        Log.debug("Result: " + result);
        return result;
    }
}
