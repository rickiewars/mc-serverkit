package net.rickiewars.datetime.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.rickiewars.datetime.command.GetTimeCommand;
import net.rickiewars.datetime.command.LoginStreakCommand;
import net.rickiewars.datetime.event.*;


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
        ServerLifecycleEvents.SERVER_STARTING.register(new ServerStartingEvent());
        ServerLifecycleEvents.SERVER_STARTED.register(new ServerStartedEvent());
        ServerLifecycleEvents.SERVER_STOPPING.register(new ServerStoppingEvent());
    }

    private static void registerStatistics() {
        ModStatistics.register();
    }

}
