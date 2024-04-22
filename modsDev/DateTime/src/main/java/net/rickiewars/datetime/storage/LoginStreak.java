package net.rickiewars.datetime.storage;

import net.rickiewars.datetime.util.IEntityDataSaver;

public class LoginStreak {
    final static private String nbtTag = "LoginStreak";

    public static int get(IEntityDataSaver player) {
        return player.getPersistentData().getInt(nbtTag);
    }

    public static void set(IEntityDataSaver player, int streak) {
        player.getPersistentData().putInt(nbtTag, streak);
    }

    public static void remove(IEntityDataSaver player) {
        player.getPersistentData().remove(nbtTag);
    }

    public static void increment(IEntityDataSaver player) {
        int streak = get(player) + 1;
        set(player, streak);
    }

    public static void reset(IEntityDataSaver player) {
        set(player, 1);
    }


}
