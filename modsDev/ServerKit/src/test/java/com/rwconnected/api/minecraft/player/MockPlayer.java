package com.rwconnected.api.minecraft.player;

import com.rwconnected.api.minecraft.storage.MockIntStorage;
import com.rwconnected.api.minecraft.storage.MockStringStorage;
import com.rwconnected.serverkit.api.minecraft.player.IPlayer;
import com.rwconnected.serverkit.api.minecraft.storage.virtual.INumericStorage;
import com.rwconnected.serverkit.api.minecraft.storage.virtual.IStorage;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.BiConsumer;

public class MockPlayer implements IPlayer {

    public String name;
    public UUID uuid;
    public INumericStorage<Integer> loginStreakStorage;
    public INumericStorage<Integer> loginStreakRecordStorage;
    public IStorage<String> lastLoginDateStorage;
    public @Nullable BiConsumer<MessageLevel, String> messageConsumer;


    public MockPlayer(
        String name,
        UUID uuid,
        INumericStorage<Integer> loginStreakStorage,
        INumericStorage<Integer> loginStreakRecordStorage,
        IStorage<String> lastLoginDateStorage,
        @Nullable BiConsumer<MessageLevel, String> messageConsumer
    ) {
        this.name = name;
        this.uuid = uuid;
        this.loginStreakStorage = loginStreakStorage;
        this.loginStreakRecordStorage = loginStreakRecordStorage;
        this.lastLoginDateStorage = lastLoginDateStorage;
        this.messageConsumer = messageConsumer;
    }
    public MockPlayer(String name, UUID uuid) {
        this(
            name,
            uuid,
            new MockIntStorage(0, 1),
            new MockIntStorage(0),
            new MockStringStorage(),
            null
        );
    }
    public MockPlayer(String name) {
        this(name, UUID.randomUUID());
    }
    public MockPlayer() {
        this("Steve");
    }

    public enum MessageLevel {
        MESSAGE,
        INFO,
        WARNING,
        ERROR,
        SUCCESS
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public INumericStorage<Integer> getLoginStreakStorage() {
        return loginStreakStorage;
    }

    @Override
    public INumericStorage<Integer> getLoginStreakRecordStorage() {
        return loginStreakRecordStorage;
    }

    @Override
    public IStorage<String> getLastLoginDateStorage() {
        return lastLoginDateStorage;
    }

    @Override
    public void sendMessage(String message) {
        if (messageConsumer == null) return;
        messageConsumer.accept(MessageLevel.MESSAGE, message);
    }

    @Override
    public void sendInfo(String message) {
        if (messageConsumer == null) return;
        messageConsumer.accept(MessageLevel.INFO, message);
    }

    @Override
    public void sendWarning(String message) {
        if (messageConsumer == null) return;
        messageConsumer.accept(MessageLevel.WARNING, message);
    }

    @Override
    public void sendError(String message) {
        if (messageConsumer == null) return;
        messageConsumer.accept(MessageLevel.ERROR, message);
    }

    @Override
    public void sendSuccess(String message) {
        if (messageConsumer == null) return;
        messageConsumer.accept(MessageLevel.SUCCESS, message);
    }
}
