package com.rwconnected.serverkit.api.minecraft.player;

import com.mojang.brigadier.context.CommandContext;
import com.rwconnected.serverkit.api.minecraft.storage.ILastLoginDateStorage;
import com.rwconnected.serverkit.api.minecraft.storage.ILoginStreakRecordStorage;
import com.rwconnected.serverkit.api.minecraft.storage.ILoginStreakStorage;
import com.rwconnected.serverkit.api.minecraft.storage.virtual.INumericStorage;
import com.rwconnected.serverkit.api.minecraft.storage.virtual.IStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class Player implements IPlayer<ServerPlayer> {
    ServerPlayer player;

    public Player(@NotNull ServerPlayer player) {
        this.player = player;
    }
    public Player(CommandContext<CommandSourceStack> context) {
        this(Objects.requireNonNull(context.getSource().getPlayer()));
    }

    @Override
    public String getName() {
        return player.getName().getString();
    }

    @Override
    public UUID getUUID() {
        return player.getUUID();
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
        player.sendSystemMessage(Component.literal(message));
    }

    @Override
    public void sendInfo(String message) {
        player.createCommandSourceStack().sendSuccess(
            () -> Component.literal(message).withStyle(ChatFormatting.BLUE),
            false);
    }

    @Override
    public void sendWarning(String message) {
        player.createCommandSourceStack().sendSuccess(
            () -> Component.literal(message).withStyle(ChatFormatting.YELLOW),
            false);
    }

    @Override
    public void sendError(String message) {
        player.createCommandSourceStack().sendSuccess(
            () -> Component.literal(message).withStyle(ChatFormatting.RED),
            false);
    }

    @Override
    public void sendSuccess(String message) {
        player.createCommandSourceStack().sendSuccess(
            () -> Component.literal(message).withStyle(ChatFormatting.GREEN),
            false);
    }

    @Override
    public ServerPlayer getSource() {
        return player;
    }
}
