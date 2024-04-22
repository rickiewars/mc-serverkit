package net.rickiewars.datetime.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.rickiewars.datetime.command.GetTimeCommand;
import net.rickiewars.datetime.command.LoginStreakCommand;
import net.rickiewars.datetime.event.PlayerCopyFromEvent;
import net.rickiewars.datetime.event.PlayerJoinEvent;


public class ModRegistries {
    public static void register() {
        registerCommands();
        registerEvents();
        registerStatistics();
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(GetTimeCommand::register);
        CommandRegistrationCallback.EVENT.register(LoginStreakCommand::register);
    }

    private static void registerEvents() {
        ServerPlayerEvents.COPY_FROM.register(new PlayerCopyFromEvent());
        ServerPlayConnectionEvents.JOIN.register(new PlayerJoinEvent());
    }

    private static void registerStatistics() {
        ModStatistics.register();
    }

}
