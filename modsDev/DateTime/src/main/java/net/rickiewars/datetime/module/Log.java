package net.rickiewars.datetime.module;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.rickiewars.datetime.DateTime;

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
                DateTime.LOGGER.debug(message);
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
                DateTime.LOGGER.info(message);
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
                DateTime.LOGGER.warn(message);
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
                DateTime.LOGGER.error(message);
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
