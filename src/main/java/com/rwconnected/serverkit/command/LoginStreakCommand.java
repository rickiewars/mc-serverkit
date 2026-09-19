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
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LoginStreakCommand {
    public static void register(CommandDispatcher<CommandSourceStack> serverCommandSourceCommandDispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(Commands.literal("loginStreak")
            .requires(Permission.LOGIN_STREAK.require())
            .executes(ctx -> getStreak(ctx, false))
            .then(Commands.literal("help")
                .requires(Permission.LOGIN_STREAK_HELP.require())
                .executes(context -> {
                    Log.source(context, Config.instance().loginStreak.welcomeMessage());
                    return 1;
                })
            ).then(Commands.literal("get")
                .requires(Permission.LOGIN_STREAK_GET.require())
                .executes(ctx -> getStreak(ctx, false))
                .then(Commands.argument("player", EntityArgument.player())
                    .requires(Permission.LOGIN_STREAK_GET_PLAYER.require())
                    .executes(ctx -> getStreak(ctx, true))
                )
            ).then(Commands.literal("set")
                .requires(Permission.LOGIN_STREAK_SET.require())
                .then(Commands.literal("streak")
                    .then(Commands.argument("streak", IntegerArgumentType.integer())
                        .executes(ctx -> setStreak(ctx, false))
                        .then(Commands.argument("player", EntityArgument.player())
                            .requires(Permission.LOGIN_STREAK_SET_PLAYER.require())
                            .executes(ctx -> setStreak(ctx, true))
                        )
                    )
                ).then(Commands.literal("record")
                    .then(Commands.argument("record", IntegerArgumentType.integer())
                        .executes(ctx -> setRecord(ctx, false))
                        .then(Commands.argument("player", EntityArgument.player())
                            .requires(Permission.LOGIN_STREAK_SET_PLAYER.require())
                            .executes(ctx -> setRecord(ctx, true))
                        )
                    )
                )

            ).then(Commands.literal("milestones")
                .requires(Permission.LOGIN_STREAK_MILESTONES.require())
                .executes(LoginStreakCommand::showMilestones)
            ).then(Commands.literal("reward")
                .requires(Permission.LOGIN_STREAK_REWARD.require())
                .then(Commands.argument("player", EntityArgument.player())
                    .then(Commands.argument("amount", IntegerArgumentType.integer())
                        .executes(LoginStreakCommand::reward)
                    )
                )
            ).then(Commands.literal("simulate-new-day")
                .requires(Permission.LOGIN_STREAK_TEST.require())
                .then(Commands.argument("player", EntityArgument.player())
                    .then(Commands.argument("date", StringArgumentType.string())
                        .executes(LoginStreakCommand::simulateNewDay)
                    )
                )
            )
        );
    }

    private static int getStreak(
        CommandContext<CommandSourceStack> context,
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
        CommandContext<CommandSourceStack> context,
        boolean withTarget
    ) throws CommandSyntaxException {
        Player player = getPlayer(context, withTarget);
        final int streak = IntegerArgumentType.getInteger(context, "streak");
        int result = getService().setStreak(player, streak);
        Log.source(context, "The login streak of " + player.getName() + " is set to " + result + " days.");
        return result;
    }

    private static int setRecord(
        CommandContext<CommandSourceStack> context,
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
        CommandContext<CommandSourceStack> ctx,
        boolean withTarget
    ) throws CommandSyntaxException {
        return withTarget ? new Player(Objects.requireNonNull(
                EntityArgument.getPlayer(ctx, "player")
            )) : new Player(ctx);
    }

    private static int showMilestones(
        CommandContext<CommandSourceStack> context
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

        List<MutableComponent> lines = new ArrayList<>();
        if (!milestones.isEmpty()) {
            lines.add(Component.literal("Regular login streak milestones:"));
            for (Config.LoginStreakConfig.LoginStreakMilestone milestone : milestones) {
                String checkbox = record >= milestone.days() ? "☑ " : "☐ ";
                MutableComponent line = Component.literal(checkbox + milestone.days() + ": " + milestone.formattedReward());
                if (record >= milestone.days()) {
                    line = line.withStyle(ChatFormatting.GREEN);
                }
                lines.add(line);
            }
        }
        lines.add(Component.literal(""));
        if (!periodicMilestones.isEmpty()) {
            lines.add(Component.literal("Periodic login streak milestones:"));
            for (Config.LoginStreakConfig.LoginStreakMilestone milestone : periodicMilestones) {
                lines.add(Component.literal("- " + milestone.days() + " days: " + milestone.formattedReward()));
            }
        }
        lines.add(Component.literal(""));
        lines.add(Component.literal("Your current streak is " + streak + " days."));
        lines.add(Component.literal("Your current record is " + record + " days."));

        // Send the lines to the player
        for (MutableComponent line : lines) {
            context.getSource().sendSuccess(() -> line,false);
        }

        return 1;
    }

    private static int reward(
        CommandContext<CommandSourceStack> context
    ) throws CommandSyntaxException {
        Player player = new Player(EntityArgument.getPlayer(context, "player"));
        final BigInteger amount = BigInteger.valueOf(IntegerArgumentType.getInteger(context, "amount"));
        BigInteger result = getService().reward(player, amount);
        Log.source(context, "Rewarded " + player.getName() + " with " + ModUtils.formatCurrency(result));
        return 1;
    }

    private static int simulateNewDay(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String dateStr = StringArgumentType.getString(context, "date");
        Player player = new Player(EntityArgument.getPlayer(context, "player"));

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
