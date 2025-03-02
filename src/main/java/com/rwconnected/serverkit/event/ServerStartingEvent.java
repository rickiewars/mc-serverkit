package com.rwconnected.serverkit.event;

import com.rwconnected.serverkit.ServerKit;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ServerStartingEvent implements ServerLifecycleEvents.ServerStarting {
    @Override
    public void onServerStarting(MinecraftServer server) {
        ServerKit.initServer(server);
    }
}
