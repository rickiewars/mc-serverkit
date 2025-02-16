package com.rwconnected.serverkit.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.rwconnected.serverkit.storage.LoginStreak;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import com.rwconnected.serverkit.module.Log;
import com.rwconnected.serverkit.module.LoginStreakModule;
import com.rwconnected.serverkit.util.IEntityDataSaver;

public class LoginStreakCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("loginStreak")
            .executes(LoginStreakCommand::getStreak)
            .then(CommandManager.literal("help")
                .executes(context -> {
                    Log.source(context, LoginStreakModule.getHelpMessage());
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

    public static int getStreak(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            Log.source(context, "Error: User not found.");
            return 0;
        }
        int loginStreak = LoginStreak.get((IEntityDataSaver) player);
        Log.source(context, "You have a login streak of " + loginStreak + " days.");
        return loginStreak;
    }

    // Should only be used for debugging purposes or to correct a failure
    public static int setStreak(CommandContext<ServerCommandSource> context) {
        final int streak = IntegerArgumentType.getInteger(context, "streak");
        ServerPlayerEntity player = context.getSource().getPlayer();
        LoginStreak.set((IEntityDataSaver) player, streak);
        Log.source(context, "You have set the login streak of " + player.getName().getString() + " to " + streak + " days.");
        return 1;
    }

}
