package com.rwconnected.serverkit.api.minecraft.storage;

import com.mojang.brigadier.context.CommandContext;
import com.rwconnected.serverkit.api.minecraft.storage.virtual.NbtStringStorage;
import com.rwconnected.serverkit.util.IEntityDataSaver;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LastLoginDateStorage extends NbtStringStorage {
    public LastLoginDateStorage(IEntityDataSaver entityDataSaver) {
        super(entityDataSaver, "lastLoginDate", "");
    }
    public LastLoginDateStorage(@NotNull ServerPlayerEntity player) {
        this((IEntityDataSaver) player);
    }
    public LastLoginDateStorage(CommandContext<ServerCommandSource> context) {
        this(Objects.requireNonNull(context.getSource().getPlayer()));
    }
}
