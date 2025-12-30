package com.rwconnected.serverkit.api.minecraft.storage;

import com.rwconnected.serverkit.api.minecraft.storage.virtual.PlayerFieldStorage;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class ILastLoginDateStorage extends PlayerFieldStorage<String> {
    private final static String DEFAULT_VALUE = "";

    public ILastLoginDateStorage(@NotNull ServerPlayerEntity player) {
        super(player);
    }

    @Override
    public String get() {
        return player.serverkit_getLastLoginDate();
    }

    @Override
    public void set(String value) {
        player.serverkit_setLastLoginDate(value);
    }

    @Override
    public void reset() {
        set(DEFAULT_VALUE);
    }
}
