package com.rwconnected.serverkit.event;

import com.rwconnected.serverkit.ServerKit;
import com.rwconnected.serverkit.config.Config;
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
            HttpServerHandler.getInstance().startServer(
                Config.instance().httpServer.host(),
                Config.instance().httpServer.port()
            );
            ServerKit.LOGGER.info("HTTP server started successfully.");
        } catch (IOException e) {
            ServerKit.LOGGER.error("Failed to start HTTP server", e);
        }
    }
}
