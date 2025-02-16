package com.rwconnected.serverkit.event;

import com.rwconnected.serverkit.ServerKit;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import com.rwconnected.serverkit.module.http.HttpServerHandler;
import com.rwconnected.serverkit.util.ModServices;

import java.io.IOException;

public class ServerStartedEvent implements ServerLifecycleEvents.ServerStarted {
    @Override
    public void onServerStarted(MinecraftServer server) {
        ModServices.register();

        try {
            HttpServerHandler.getInstance().startServer("0.0.0.0", 8081);
            ServerKit.LOGGER.info("HTTP server started successfully.");
        } catch (IOException e) {
            ServerKit.LOGGER.error("Failed to start HTTP server", e);
        }
    }
}
