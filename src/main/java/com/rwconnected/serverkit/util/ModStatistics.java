package com.rwconnected.serverkit.util;

import com.rwconnected.serverkit.ServerKit;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ModStatistics {
    public static final Identifier LOGIN_STREAK = Identifier.fromNamespaceAndPath(ServerKit.MOD_ID, "login_streak");
    public static void register() {
        Registry.register(BuiltInRegistries.CUSTOM_STAT, "login_streak", LOGIN_STREAK);
//        Stats.CUSTOM.getOrCreateStat(LOGIN_STREAK, StatFormatter.DEFAULT);
    }



}
