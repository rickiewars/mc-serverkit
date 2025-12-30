package com.rwconnected.serverkit.api.minecraft.storage.virtual;

import com.rwconnected.serverkit.api.minecraft.storage.ServerKitPlayerData;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class PlayerFieldStorage<T> implements IStorage<T> {
    protected final ServerKitPlayerData player;

    protected PlayerFieldStorage(ServerPlayerEntity player) {
        this.player = (ServerKitPlayerData) player;
    }
}
