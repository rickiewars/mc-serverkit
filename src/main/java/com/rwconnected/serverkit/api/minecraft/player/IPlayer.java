package com.rwconnected.serverkit.api.minecraft.player;

import com.rwconnected.serverkit.api.minecraft.storage.virtual.INumericStorage;
import com.rwconnected.serverkit.api.minecraft.storage.virtual.IStorage;

import java.util.UUID;

public interface IPlayer {
    public String getName();
    public UUID getUUID();

    public INumericStorage<Integer> getLoginStreakStorage();
    public INumericStorage<Integer> getLoginStreakRecordStorage();
    public IStorage<String> getLastLoginDateStorage();

    public void sendMessage(String message);
    public void sendInfo(String message);
    public void sendWarning(String message);
    public void sendError(String message);
    public void sendSuccess(String message);
}
