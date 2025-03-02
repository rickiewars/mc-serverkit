package com.rwconnected.serverkit.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.rwconnected.serverkit.api.minecraft.storage.LoginStreakStorage;
import com.rwconnected.serverkit.config.Config;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import com.rwconnected.serverkit.module.Log;

import java.util.Objects;

public class LoginStreakCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("loginStreak")
            .executes(LoginStreakCommand::getStreak)
            .then(CommandManager.literal("help")
                .executes(context -> {
                    Log.source(context, Config.instance().loginStreak.welcomeMessage());
                    return 1;
                })
            ).then(CommandManager.literal("set")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("streak", IntegerArgumentType.integer())
                    .executes(LoginStreakCommand::setStreak)
                )
            )
        );
    }

    private static int getStreak(CommandContext<ServerCommandSource> context) {
        LoginStreakStorage storage = new LoginStreakStorage(context);
        int streak = storage.get();
        Log.source(context, "You have a login streak of " + streak + " days.");
        return streak;
    }

    // Should only be used for debugging purposes or to correct a failure
    private static int setStreak(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = Objects.requireNonNull(context.getSource().getPlayer());
        LoginStreakStorage storage = new LoginStreakStorage(player);
        final int streak = IntegerArgumentType.getInteger(context, "streak");
        storage.set(streak);
        Log.source(context, "You have set the login streak of " + player.getName() + " to " + streak + " days.");
        return streak;
    }

}
