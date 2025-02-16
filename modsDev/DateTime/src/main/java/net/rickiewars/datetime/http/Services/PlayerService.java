package net.rickiewars.datetime.http.Services;

import net.minecraft.server.MinecraftServer;
import java.util.concurrent.CompletableFuture;

public class PlayerService {
    private final MinecraftServer server;

    public PlayerService(MinecraftServer server) {
        this.server = server;
    }

    public CompletableFuture<String[]> listOnlinePlayers() {
        CompletableFuture<String[]> future = new CompletableFuture<>();
        this.server.execute(() -> future.complete(server.getPlayerNames()));
        return future;
    }
}
