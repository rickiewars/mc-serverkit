package com.rwconnected.serverkit.util;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import com.rwconnected.serverkit.ServerKit;

public class ModStatistics {
    public static final Identifier LOGIN_STREAK = Identifier.of(ServerKit.MOD_ID, "login_streak");
    public static void register() {
        Registry.register(Registries.CUSTOM_STAT, "login_streak", LOGIN_STREAK);
//        Stats.CUSTOM.getOrCreateStat(LOGIN_STREAK, StatFormatter.DEFAULT);
    }



}
