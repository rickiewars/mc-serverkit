package com.rwconnected.serverkit.api.minecraft.player;

import com.mojang.brigadier.context.CommandContext;
import com.rwconnected.serverkit.api.minecraft.storage.ILastLoginDateStorage;
import com.rwconnected.serverkit.api.minecraft.storage.ILoginStreakRecordStorage;
import com.rwconnected.serverkit.api.minecraft.storage.ILoginStreakStorage;
import com.rwconnected.serverkit.api.minecraft.storage.virtual.INumericStorage;
import com.rwconnected.serverkit.api.minecraft.storage.virtual.IStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class Player implements IPlayer<ServerPlayerEntity> {
    ServerPlayerEntity player;

    public Player(@NotNull ServerPlayerEntity player) {
        this.player = player;
    }
    public Player(CommandContext<ServerCommandSource> context) {
        this(Objects.requireNonNull(context.getSource().getPlayer()));
    }

    @Override
    public String getName() {
        return player.getName().getString();
    }

    @Override
    public UUID getUUID() {
        return player.getUuid();
    }

    @Override
    public INumericStorage<Integer> getLoginStreakStorage() {
        return new ILoginStreakStorage(player);
    }

    @Override
    public INumericStorage<Integer> getLoginStreakRecordStorage() {
        return new ILoginStreakRecordStorage(player);
    }

    @Override
    public IStorage<String> getLastLoginDateStorage() {
        return new ILastLoginDateStorage(player);
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(Text.of(message), false);
    }

    @Override
    public void sendInfo(String message) {
        player.getCommandSource().sendFeedback(
            () -> Text.literal(message).formatted(Formatting.BLUE),
            false);
    }

    @Override
    public void sendWarning(String message) {
        player.getCommandSource().sendFeedback(
            () -> Text.literal(message).formatted(Formatting.YELLOW),
            false);
    }

    @Override
    public void sendError(String message) {
        player.getCommandSource().sendFeedback(
            () -> Text.literal(message).formatted(Formatting.RED),
            false);
    }

    @Override
    public void sendSuccess(String message) {
        player.getCommandSource().sendFeedback(
            () -> Text.literal(message).formatted(Formatting.GREEN),
            false);
    }

    @Override
    public ServerPlayerEntity getSource() {
        return player;
    }
}
