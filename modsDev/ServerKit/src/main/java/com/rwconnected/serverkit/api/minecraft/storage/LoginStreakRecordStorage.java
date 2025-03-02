package com.rwconnected.serverkit.api.minecraft.storage;

import com.mojang.brigadier.context.CommandContext;
import com.rwconnected.serverkit.api.minecraft.storage.virtual.NbtIntStorage;
import com.rwconnected.serverkit.util.IEntityDataSaver;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoginStreakRecordStorage extends NbtIntStorage {
    public LoginStreakRecordStorage(IEntityDataSaver entityDataSaver) {
        super(entityDataSaver, "bestLoginStreak", 1);
    }
    public LoginStreakRecordStorage(@NotNull ServerPlayerEntity player) {
        this((IEntityDataSaver) player);
    }
    public LoginStreakRecordStorage(CommandContext<ServerCommandSource> context) {
        this(Objects.requireNonNull(context.getSource().getPlayer()));
    }
}
