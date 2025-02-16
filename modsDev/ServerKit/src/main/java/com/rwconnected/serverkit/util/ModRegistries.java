package com.rwconnected.serverkit.util;

import com.rwconnected.serverkit.command.GetTimeCommand;
import com.rwconnected.serverkit.command.LoginStreakCommand;
import com.rwconnected.serverkit.event.*;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;


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
