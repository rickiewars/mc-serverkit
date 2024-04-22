package net.rickiewars.datetime.storage;

import net.rickiewars.datetime.util.IEntityDataSaver;

public class LastLoginDate {

    final static private String nbtTag = "lastLoginDate";
    public static String get(IEntityDataSaver player) {
        return player.getPersistentData().getString(nbtTag);
    }

    public static void set(IEntityDataSaver player, String date) {
        player.getPersistentData().putString(nbtTag, date);
    }

    public static void remove(IEntityDataSaver player) {
        player.getPersistentData().remove(nbtTag);
    }

}
