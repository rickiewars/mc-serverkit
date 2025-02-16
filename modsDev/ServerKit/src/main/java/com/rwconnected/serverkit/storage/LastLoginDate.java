package com.rwconnected.serverkit.storage;

import com.rwconnected.serverkit.util.IEntityDataSaver;

public class LastLoginDate {

    final static private String nbtTag = "lastLoginDate";
    public static String get(IEntityDataSaver player) {
        return player.serverkit_getPersistentData().getString(nbtTag);
    }

    public static void set(IEntityDataSaver player, String date) {
        player.serverkit_getPersistentData().putString(nbtTag, date);
    }

    public static void remove(IEntityDataSaver player) {
        player.serverkit_getPersistentData().remove(nbtTag);
    }

}
