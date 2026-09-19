package com.rwconnected.serverkit.errors;

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class CommandErrors {
    private CommandErrors() {}

    public static final DynamicCommandExceptionType DATE_PARSE_ERROR =
        new DynamicCommandExceptionType(date -> Component.literal(
            "Could not parse date: " + date
        ).withStyle(ChatFormatting.RED));
}
