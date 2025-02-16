package net.rickiewars.datetime.storage;

import net.rickiewars.datetime.util.IEntityDataSaver;

public class LastLoginDate {

    final static private String nbtTag = "lastLoginDate";
    public static String get(IEntityDataSaver player) {
        return player.datetime_getPersistentData().getString(nbtTag);
    }

    public static void set(IEntityDataSaver player, String date) {
        player.datetime_getPersistentData().putString(nbtTag, date);
    }

    public static void remove(IEntityDataSaver player) {
        player.datetime_getPersistentData().remove(nbtTag);
    }

}
