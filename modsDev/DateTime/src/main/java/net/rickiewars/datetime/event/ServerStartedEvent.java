package net.rickiewars.datetime.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.rickiewars.datetime.DateTime;
import net.rickiewars.datetime.module.http.HttpServerHandler;
import net.rickiewars.datetime.util.ModServices;

import java.io.IOException;

public class ServerStartedEvent implements ServerLifecycleEvents.ServerStarted {
    @Override
    public void onServerStarted(MinecraftServer server) {
        ModServices.register();

        try {
            HttpServerHandler.getInstance().startServer("0.0.0.0", 8081);
            DateTime.LOGGER.info("HTTP server started successfully.");
        } catch (IOException e) {
            DateTime.LOGGER.error("Failed to start HTTP server", e);
        }
    }
}
