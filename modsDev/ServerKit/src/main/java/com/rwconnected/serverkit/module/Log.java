package com.rwconnected.serverkit.module;

import com.mojang.brigadier.context.CommandContext;
import com.rwconnected.serverkit.ServerKit;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class Log {

    private enum logLevel {
        DEBUG,
        INFO,
        WARNING,
        ERROR,
        NONE
    }
    private static final logLevel sendFeedbackTo = logLevel.INFO;

    public static void debug(String message) {
        switch (sendFeedbackTo) {
            case DEBUG:
                ServerKit.LOGGER.debug(message);
            case INFO:
            case ERROR:
            case NONE:
            default:
                break;
        }
    }

    public static void info(String message) {
        switch (sendFeedbackTo) {
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
        switch (sendFeedbackTo) {
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
        switch (sendFeedbackTo) {
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

    public static void source(CommandContext<ServerCommandSource> context, String message) {
        context.getSource().sendFeedback(() -> Text.literal(message), false);
    }

}
