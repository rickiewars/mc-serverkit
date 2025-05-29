package com.rwconnected.serverkit.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GetTimeCommand {

    private static final Map<String, String> TIME_FORMATS = new HashMap<>();
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static {
        TIME_FORMATS.put("date", "yyyy-MM-dd");
        TIME_FORMATS.put("time", "HH:mm:ss");
        TIME_FORMATS.put("timezone", "z");
        TIME_FORMATS.put("year", "yyyy");
        TIME_FORMATS.put("month", "MM");
        TIME_FORMATS.put("month_full", "MMMM");
        TIME_FORMATS.put("month_short", "MMM");
        TIME_FORMATS.put("week", "ww");
        TIME_FORMATS.put("day", "dd");
        TIME_FORMATS.put("day_of_year", "D");
        TIME_FORMATS.put("weekday", "u");
        TIME_FORMATS.put("weekday_full", "EEEE");
        TIME_FORMATS.put("weekday_short", "EE");
        TIME_FORMATS.put("hour", "HH");
        TIME_FORMATS.put("minute", "mm");
        TIME_FORMATS.put("second", "ss");
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        var rtcCommand = CommandManager.literal("rtc")
            .requires(Permission.RTC.require())
            .executes(GetTimeCommand::help);

        var getCommand = CommandManager.literal("get")
            .executes(ctx -> sendFormattedTime(ctx, null));

        TIME_FORMATS.forEach((key, format) -> {
            if (key.equals("serverkit")) return;
            getCommand.then(CommandManager.literal(key)
                .executes(ctx -> sendFormattedTime(ctx, key)));
        });

        rtcCommand.then(getCommand);
        dispatcher.register(rtcCommand);
    }

    private static int sendFormattedTime(CommandContext<ServerCommandSource> context, @Nullable String key) {
        String format = key == null ? DEFAULT_FORMAT : TIME_FORMATS.get(key);
        if (format != null) {
            String formattedTime = new SimpleDateFormat(format).format(new Date());
            context.getSource().sendFeedback(() -> Text.literal(formattedTime), false);
            try {
                return Integer.parseInt(formattedTime);
            } catch (NumberFormatException e) {
                return 1;
            }
        }
        return 0;
    }

    private static int help(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal("Usage: /rtc get ["
            + String.join("|", TIME_FORMATS.keySet()) + "]"), false);
        return 1;
    }


}
