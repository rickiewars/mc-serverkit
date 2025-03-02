package com.rwconnected.serverkit.api.minecraft.storage;

import com.mojang.brigadier.context.CommandContext;
import com.rwconnected.serverkit.api.minecraft.storage.virtual.NbtIntStorage;
import com.rwconnected.serverkit.util.IEntityDataSaver;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoginStreakStorage extends NbtIntStorage {
    public LoginStreakStorage(IEntityDataSaver entityDataSaver) {
        super(entityDataSaver, "loginStreak", 1);
    }
    public LoginStreakStorage(@NotNull ServerPlayerEntity player) {
        this((IEntityDataSaver) player);
    }
    public LoginStreakStorage(CommandContext<ServerCommandSource> context) {
        this(Objects.requireNonNull(context.getSource().getPlayer()));
    }
}
