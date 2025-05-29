package com.rwconnected.serverkit.command;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Vanilla permission levels:
 * 0: Normal player
 * 1: Moderators can bypass spawn protection
 * 2: Gamemasters have access to commands like /scoreboard, /advancement, /function, etc. and can use command blocks
 * 3: Admins have access to most commands including /op and /deop. So effectively these players can do anything.
 * 4: Owner has access to all commands
 */
public enum Permission {
    LOGIN_STREAK("loginStreak", 0),
    LOGIN_STREAK_HELP("loginStreak.help", 0),
    LOGIN_STREAK_MILESTONES("loginStreak.milestones", 0),
    LOGIN_STREAK_GET("loginStreak.get", 0),
    LOGIN_STREAK_GET_PLAYER("loginStreak.get.player", 1),
    LOGIN_STREAK_SET("loginStreak.set", 3),
    LOGIN_STREAK_SET_PLAYER("loginStreak.set.player", 3),
    LOGIN_STREAK_REWARD("loginStreak.reward", 3),

    RTC("rtc", 2);

    private final String permission;
    private final int defaultLevel;

    Permission(String permission, int defaultLevel) {
        this.permission = permission;
        this.defaultLevel = defaultLevel;
    }

    @NotNull
    public Predicate<ServerCommandSource> require() {
        return Permissions.require(this.permission, this.defaultLevel);
    }
}
