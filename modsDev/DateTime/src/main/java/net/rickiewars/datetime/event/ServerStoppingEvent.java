package net.rickiewars.datetime.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.rickiewars.datetime.module.http.HttpServerHandler;

public class ServerStoppingEvent implements ServerLifecycleEvents.ServerStopping {
    @Override
    public void onServerStopping(MinecraftServer server) {
        HttpServerHandler.getInstance().stopServer();
    }
}
