package net.rickiewars.datetime.util;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.rickiewars.datetime.DateTime;

public class ModStatistics {
    public static final Identifier LOGIN_STREAK = new Identifier(DateTime.MOD_ID, "login_streak");
    public static void register() {
        Registry.register(Registries.CUSTOM_STAT, "login_streak", LOGIN_STREAK);
//        Stats.CUSTOM.getOrCreateStat(LOGIN_STREAK, StatFormatter.DEFAULT);
    }



}
