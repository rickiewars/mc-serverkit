package net.rickiewars.datetime.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ScoreboardCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.rickiewars.datetime.util.IEntityDataSaver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetTimeCommand {

    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("rtc")
                .executes(GetTimeCommand::help)
                .then(CommandManager.literal("test")
                        .executes(GetTimeCommand::testDbg))
                .then(CommandManager.literal("get")
                        .executes(GetTimeCommand::dateTime)
                        .then(CommandManager.literal("date").executes(GetTimeCommand::date))
                        .then(CommandManager.literal("time").executes(GetTimeCommand::time))
                        .then(CommandManager.literal("year").executes(GetTimeCommand::year))
                        .then(CommandManager.literal("month").executes(GetTimeCommand::month)
                                .then(CommandManager.literal("number").executes(GetTimeCommand::month))
                                .then(CommandManager.literal("full").executes(GetTimeCommand::monthFull))
                                .then(CommandManager.literal("short").executes(GetTimeCommand::monthShort))
                        )
                        .then(CommandManager.literal("week").executes(GetTimeCommand::week))
                        .then(CommandManager.literal("day").executes(GetTimeCommand::day)
                                .then(CommandManager.literal("of")
                                        .then(CommandManager.literal("week").executes(GetTimeCommand::weekDay)
                                                .then(CommandManager.literal("number").executes(GetTimeCommand::weekDay))
                                                .then(CommandManager.literal("full").executes(GetTimeCommand::weekDayFull))
                                                .then(CommandManager.literal("short").executes(GetTimeCommand::weekDayShort))
                                        )
                                        .then(CommandManager.literal("month").executes(GetTimeCommand::day))
                                        .then(CommandManager.literal("year").executes(GetTimeCommand::yearDay))
                        ))
                        .then(CommandManager.literal("hour").executes(GetTimeCommand::hour))
                        .then(CommandManager.literal("minute").executes(GetTimeCommand::minute))
                        .then(CommandManager.literal("second").executes(GetTimeCommand::second))
                )
        );
    }

    private static String getFormattedTime(String format) {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(currentDate);
    }

    private static int testDbg(CommandContext<ServerCommandSource> context) {
        // Print the count of logins in the loginStreak NBT tag
        IEntityDataSaver player = (IEntityDataSaver)context.getSource().getPlayer();
        int loginStreak = player.getPersistentData().getInt("loginStreak");
        context.getSource().sendFeedback(() -> Text.literal("loginStreak: " + loginStreak), false);
        return 1;
    }

    private static int dateTime(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal(getFormattedTime("yyyy-MM-dd HH:mm:ss")), false);
        return 1;
    }

    private static int date(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal(getFormattedTime("yyyy-MM-dd")), false);
        return 1;
    }

    private static int time(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal(getFormattedTime("HH:mm:ss")), false);
        return 1;
    }

    private static int year(CommandContext<ServerCommandSource> context) {
        String year = getFormattedTime("yyyy");
        context.getSource().sendFeedback(() -> Text.literal(year), false);
        return Integer.parseInt(year);
    }

    private static int month(CommandContext<ServerCommandSource> context) {
        String month = getFormattedTime("MM");
        context.getSource().sendFeedback(() -> Text.literal(month), false);
        return Integer.parseInt(month);
    }

    private static int monthFull(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal(getFormattedTime("MMMM")), false);
        return 1;
    }

    private static int monthShort(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal(getFormattedTime("MMM")), false);
        return 1;
    }

    private static int week(CommandContext<ServerCommandSource> context) {
        String week = getFormattedTime("ww");
        context.getSource().sendFeedback(() -> Text.literal(week), false);
        return Integer.parseInt(week);
    }

    private static int day(CommandContext<ServerCommandSource> context) {
        String day = getFormattedTime("dd");
        context.getSource().sendFeedback(() -> Text.literal(day), false);
        return Integer.parseInt(day);
    }

    private static int yearDay(CommandContext<ServerCommandSource> context) {
        String day = getFormattedTime("D");
        context.getSource().sendFeedback(() -> Text.literal(day), false);
        return Integer.parseInt(day);
    }

    private static int weekDay(CommandContext<ServerCommandSource> context) {
        String day = getFormattedTime("u");
        context.getSource().sendFeedback(() -> Text.literal(day), false);
        return Integer.parseInt(day);
    }

    private static int weekDayFull(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal(getFormattedTime("EEEE")), false);
        return 1;
    }

    private static int weekDayShort(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal(getFormattedTime("EE")), false);
        return 1;
    }

    private static int hour(CommandContext<ServerCommandSource> context) {
        String hour = getFormattedTime("HH");
        context.getSource().sendFeedback(() -> Text.literal(hour), false);
        return Integer.parseInt(hour);
    }

    private static int minute(CommandContext<ServerCommandSource> context) {
        String minute = getFormattedTime("mm");
        context.getSource().sendFeedback(() -> Text.literal(minute), false);
        return Integer.parseInt(minute);
    }

    private static int second(CommandContext<ServerCommandSource> context) {
        String second = getFormattedTime("ss");
        context.getSource().sendFeedback(() -> Text.literal(second), false);
        return Integer.parseInt(second);
    }

    private static int help(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal("Usage: /rtc get [date|time|year|month|week|day|hour|minute|second]"), false);
        return 1;
    }


}
