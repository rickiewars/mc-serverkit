package net.rickiewars.datetime.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.rickiewars.datetime.module.Log;
import net.rickiewars.datetime.module.LoginStreakModule;
import net.rickiewars.datetime.storage.LoginStreak;
import net.rickiewars.datetime.util.IEntityDataSaver;

public class LoginStreakCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("loginStreak")
                .executes(LoginStreakCommand::getStreak)
                .then(CommandManager.literal("help")
                        .executes(context -> {
                            Log.source(context, LoginStreakModule.getHelpMessage());
                            return 1;
                        })
                ).then(CommandManager.literal("process")
                        .requires(source -> source.hasPermissionLevel(4))
                        .executes(LoginStreakCommand::process)
                ).then(CommandManager.literal("quiet")
                        .requires(source -> source.hasPermissionLevel(4))
                        .executes(LoginStreakCommand::getStreakQuiet)
                ).then(CommandManager.literal("set")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(CommandManager.argument("streak", IntegerArgumentType.integer())
                                .executes(LoginStreakCommand::setStreak)
                        )
                )
        );
    }

    public static int process(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        return LoginStreakModule.process(player);
    }

    public static int getStreak(CommandContext<ServerCommandSource> context) {
        int loginStreak = getStreakQuiet(context);
        if (loginStreak == 0) {
            Log.source(context, "Error: User not found.");
        }
        Log.source(context, "You have a login streak of " + loginStreak + " days.");
        return loginStreak;
    }

    public static int getStreakQuiet(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            return 0;
        }
        return LoginStreak.get((IEntityDataSaver)player);
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
