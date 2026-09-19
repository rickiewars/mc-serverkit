package com.rwconnected.serverkit.module;

import com.mojang.brigadier.context.CommandContext;
import com.rwconnected.serverkit.ServerKit;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class Log {

    private enum LogLevel {
        DEBUG,
        INFO,
        WARNING,
        ERROR,
        NONE
    }
    private static final LogLevel logLevel = LogLevel.INFO;

    public static void debug(String message) {
        switch (logLevel) {
            case DEBUG:
                ServerKit.LOGGER.debug(message);
                break;
            case INFO:
            case ERROR:
            case NONE:
            default:
                break;
        }
    }

    public static void info(String message) {
        switch (logLevel) {
            case DEBUG:
            case INFO:
                ServerKit.LOGGER.info(message);
                break;
            case WARNING:
            case ERROR:
            case NONE:
            default:
                break;
        }
    }

    public static void warning(String message) {
        switch (logLevel) {
            case DEBUG:
            case INFO:
            case WARNING:
                ServerKit.LOGGER.warn(message);
                break;
            case ERROR:
            case NONE:
            default:
                break;
        }
    }

    public static void error(String message) {
        switch (logLevel) {
            case DEBUG:
            case INFO:
            case WARNING:
            case ERROR:
                ServerKit.LOGGER.error(message);
                break;
            case NONE:
            default:
                break;
        }
    }

    public static void source(CommandContext<CommandSourceStack> context, String message) {
        context.getSource().sendSuccess(() -> Component.literal(message), false);
    }

}
