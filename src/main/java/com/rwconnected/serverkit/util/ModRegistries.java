package com.rwconnected.serverkit.util;

import com.rwconnected.serverkit.command.GetTimeCommand;
import com.rwconnected.serverkit.command.LoginStreakCommand;
import com.rwconnected.serverkit.event.PlayerJoinEvent;
import com.rwconnected.serverkit.event.ServerStartedEvent;
import com.rwconnected.serverkit.event.ServerStartingEvent;
import com.rwconnected.serverkit.event.ServerStoppingEvent;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
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
        ServerPlayConnectionEvents.JOIN.register(new PlayerJoinEvent());
        ServerLifecycleEvents.SERVER_STARTING.register(new ServerStartingEvent());
        ServerLifecycleEvents.SERVER_STARTED.register(new ServerStartedEvent());
        ServerLifecycleEvents.SERVER_STOPPING.register(new ServerStoppingEvent());
    }

    private static void registerStatistics() {
        ModStatistics.register();
    }

}
