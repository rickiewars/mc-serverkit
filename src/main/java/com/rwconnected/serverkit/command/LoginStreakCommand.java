package com.rwconnected.serverkit.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.rwconnected.serverkit.ServerKit;
import com.rwconnected.serverkit.api.economy.Patbox.PbEconomyProvider;
import com.rwconnected.serverkit.api.minecraft.player.Player;
import com.rwconnected.serverkit.api.util.time.ITimeProvider;
import com.rwconnected.serverkit.api.util.time.MockTimeProvider;
import com.rwconnected.serverkit.api.util.time.SystemTimeProvider;
import com.rwconnected.serverkit.config.Config;
import com.rwconnected.serverkit.errors.CommandErrors;
import com.rwconnected.serverkit.module.Log;
import com.rwconnected.serverkit.service.LoginStreakService;
import com.rwconnected.serverkit.util.ModUtils;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LoginStreakCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("loginStreak")
            .requires(Permission.LOGIN_STREAK.require())
            .executes(ctx -> getStreak(ctx, false))
            .then(CommandManager.literal("help")
                .requires(Permission.LOGIN_STREAK_HELP.require())
                .executes(context -> {
                    Log.source(context, Config.instance().loginStreak.welcomeMessage());
                    return 1;
                })
            ).then(CommandManager.literal("get")
                .requires(Permission.LOGIN_STREAK_GET.require())
                .executes(ctx -> getStreak(ctx, false))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                    .requires(Permission.LOGIN_STREAK_GET_PLAYER.require())
                    .executes(ctx -> getStreak(ctx, true))
                )
            ).then(CommandManager.literal("set")
                .requires(Permission.LOGIN_STREAK_SET.require())
                .then(CommandManager.literal("streak")
                    .then(CommandManager.argument("streak", IntegerArgumentType.integer())
                        .executes(ctx -> setStreak(ctx, false))
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                            .requires(Permission.LOGIN_STREAK_SET_PLAYER.require())
                            .executes(ctx -> setStreak(ctx, true))
                        )
                    )
                ).then(CommandManager.literal("record")
                    .then(CommandManager.argument("record", IntegerArgumentType.integer())
                        .executes(ctx -> setRecord(ctx, false))
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                            .requires(Permission.LOGIN_STREAK_SET_PLAYER.require())
                            .executes(ctx -> setRecord(ctx, true))
                        )
                    )
                )

            ).then(CommandManager.literal("milestones")
                .requires(Permission.LOGIN_STREAK_MILESTONES.require())
                .executes(LoginStreakCommand::showMilestones)
            ).then(CommandManager.literal("reward")
                .requires(Permission.LOGIN_STREAK_REWARD.require())
                .then(CommandManager.argument("player", EntityArgumentType.player())
                    .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                        .executes(LoginStreakCommand::reward)
                    )
                )
            ).then(CommandManager.literal("simulate-new-day")
                .requires(Permission.LOGIN_STREAK_REWARD.require())
                .then(CommandManager.argument("player", EntityArgumentType.player())
                    .then(CommandManager.argument("date", StringArgumentType.string())
                        .executes(LoginStreakCommand::simulateNewDay)
                    )
                )
            )
        );
    }

    private static int getStreak(
        CommandContext<ServerCommandSource> context,
        boolean withTarget
    ) throws CommandSyntaxException {
        Player player = getPlayer(context, withTarget);
        int streak = getService().getStreak(player);
        int record = getService().getRecord(player);
        String target = withTarget ? player.getName() + " has" : "You have";
        Log.source(context, target + " a login streak of " + streak + " days.");
        Log.source(context, target + " a login streak record of " + record + " days.");
        return streak;
    }

    // Should only be used for debugging purposes or to correct a failure
    private static int setStreak(
        CommandContext<ServerCommandSource> context,
        boolean withTarget
    ) throws CommandSyntaxException {
        Player player = getPlayer(context, withTarget);
        final int streak = IntegerArgumentType.getInteger(context, "streak");
        int result = getService().setStreak(player, streak);
        Log.source(context, "The login streak of " + player.getName() + " is set to " + result + " days.");
        return result;
    }

    private static int setRecord(
        CommandContext<ServerCommandSource> context,
        boolean withTarget
    ) throws CommandSyntaxException {
        Player player = getPlayer(context, withTarget);
        final int record = IntegerArgumentType.getInteger(context, "record");
        int result = getService().setRecord(player, record);
        Log.source(context, "The login streak record of " + player.getName() + " is set to " + result + " days.");
        return result;
    }

    private static LoginStreakService getService() {
        return new LoginStreakService(new SystemTimeProvider(), new PbEconomyProvider(ServerKit.getServer()));
    }

    private static Player getPlayer(
        CommandContext<ServerCommandSource> ctx,
        boolean withTarget
    ) throws CommandSyntaxException {
        return withTarget ? new Player(Objects.requireNonNull(
                EntityArgumentType.getPlayer(ctx, "player")
            )) : new Player(ctx);
    }

    private static int showMilestones(
        CommandContext<ServerCommandSource> context
    ) throws CommandSyntaxException {
        Player player = new Player(context);
        int record = getService().getRecord(player);
        int streak = getService().getStreak(player);

        List<Config.LoginStreakConfig.LoginStreakMilestone> periodicMilestones = new LinkedList<>();
        List<Config.LoginStreakConfig.LoginStreakMilestone> milestones = new LinkedList<>();
        Config.instance().loginStreak.milestones().forEach(milestone -> {
            if (milestone.periodic()) {
                periodicMilestones.add(milestone);
            } else {
                milestones.add(milestone);
            }
        });

        List<MutableText> lines = new ArrayList<>();
        if (!milestones.isEmpty()) {
            lines.add(Text.literal("Regular login streak milestones:"));
            for (Config.LoginStreakConfig.LoginStreakMilestone milestone : milestones) {
                String checkbox = record >= milestone.days() ? "☑ " : "☐ ";
                MutableText line = Text.literal(checkbox + milestone.days() + ": " + milestone.formattedReward());
                if (record >= milestone.days()) {
                    line = line.formatted(Formatting.GREEN);
                }
                lines.add(line);
            }
        }
        lines.add(Text.literal(""));
        if (!periodicMilestones.isEmpty()) {
            lines.add(Text.literal("Periodic login streak milestones:"));
            for (Config.LoginStreakConfig.LoginStreakMilestone milestone : periodicMilestones) {
                lines.add(Text.literal("- " + milestone.days() + " days: " + milestone.formattedReward()));
            }
        }
        lines.add(Text.literal(""));
        lines.add(Text.literal("Your current streak is " + streak + " days."));
        lines.add(Text.literal("Your current record is " + record + " days."));

        // Send the lines to the player
        for (MutableText line : lines) {
            context.getSource().sendFeedback(() -> line,false);
        }

        return 1;
    }

    private static int reward(
        CommandContext<ServerCommandSource> context
    ) throws CommandSyntaxException {
        Player player = new Player(EntityArgumentType.getPlayer(context, "player"));
        final int amount = IntegerArgumentType.getInteger(context, "amount");
        int result = getService().reward(player, amount);
        Log.source(context, "Rewarded " + player.getName() + " with " + ModUtils.formatCurrency(result));
        return 1;
    }

    private static int simulateNewDay(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String dateStr = StringArgumentType.getString(context, "date");
        Player player = new Player(EntityArgumentType.getPlayer(context, "player"));

        try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormatter.parse(dateStr);
            ITimeProvider timeProvider = new MockTimeProvider(date);
            LoginStreakService service = new LoginStreakService(
                timeProvider,
                new PbEconomyProvider(ServerKit.getServer())
            );
            service.process(player);
        } catch (ParseException e) {
            throw CommandErrors.DATE_PARSE_ERROR.create(dateStr);
        }

        return 0;
    }

}
