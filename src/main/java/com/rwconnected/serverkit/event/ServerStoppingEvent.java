package com.rwconnected.serverkit.event;

import com.rwconnected.serverkit.module.http.HttpServerHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public class ServerStoppingEvent implements ServerLifecycleEvents.ServerStopping {
    @Override
    public void onServerStopping(@NotNull MinecraftServer server) {
        HttpServerHandler.getInstance().stopServer();
    }
}
