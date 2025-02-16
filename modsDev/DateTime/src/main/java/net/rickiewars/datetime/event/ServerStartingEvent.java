package net.rickiewars.datetime.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.rickiewars.datetime.DateTime;

public class ServerStartingEvent implements ServerLifecycleEvents.ServerStarting {
    @Override
    public void onServerStarting(MinecraftServer server) {
        DateTime.initServer(server);
    }
}
