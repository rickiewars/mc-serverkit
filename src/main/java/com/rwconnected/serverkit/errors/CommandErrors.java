package com.rwconnected.serverkit.errors;

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CommandErrors {
    private CommandErrors() {}

    public static final DynamicCommandExceptionType DATE_PARSE_ERROR =
        new DynamicCommandExceptionType(date -> Text.literal(
            "Could not parse date: " + date
        ).formatted(Formatting.RED));
}
