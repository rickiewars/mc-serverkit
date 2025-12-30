package com.rwconnected.serverkit.api.minecraft.storage;

public interface ServerKitPlayerData {
    int serverkit_getLoginStreak();
    void serverkit_setLoginStreak(int value);

    int serverkit_getLoginStreakRecord();
    void serverkit_setLoginStreakRecord(int value);

    String serverkit_getLastLoginDate();
    void serverkit_setLastLoginDate(String value);
}
