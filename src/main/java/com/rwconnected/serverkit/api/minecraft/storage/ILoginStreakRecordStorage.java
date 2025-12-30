package com.rwconnected.serverkit.api.minecraft.storage;

import com.rwconnected.serverkit.api.minecraft.storage.virtual.INumericStorage;
import com.rwconnected.serverkit.api.minecraft.storage.virtual.PlayerFieldStorage;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class ILoginStreakRecordStorage extends PlayerFieldStorage<Integer> implements INumericStorage<Integer> {
    private final static int DEFAULT_VALUE = 1;

    public ILoginStreakRecordStorage(@NotNull ServerPlayerEntity player) {
        super(player);
    }

    @Override
    public Integer get() {
        return player.serverkit_getLoginStreakRecord();
    }

    @Override
    public void set(Integer value) {
        player.serverkit_setLoginStreakRecord(value);
    }

    @Override
    public void increment() {
        set(get() + 1);
    }

    @Override
    public void decrement() {
        set(get() - 1);
    }

    @Override
    public void reset() {
        set(DEFAULT_VALUE);
    }
}
