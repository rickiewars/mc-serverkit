package com.rwconnected.serverkit.event;


import com.rwconnected.serverkit.api.minecraft.storage.LoginStreakRecordStorage;
import com.rwconnected.serverkit.api.minecraft.storage.LastLoginDateStorage;
import com.rwconnected.serverkit.api.minecraft.storage.LoginStreakStorage;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerCopyFromEvent implements ServerPlayerEvents.CopyFrom {

    /**
     * Copy the custom NBT data from the old player to the new player
     * This is for example called when a player respawns after dying
     * @param oldPlayer The old player
     * @param newPlayer The new player
     * @param alive If the player is alive
     */
    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        (new LoginStreakStorage(newPlayer)).copyFrom(oldPlayer);
        (new LastLoginDateStorage(newPlayer)).copyFrom(oldPlayer);
        (new LoginStreakRecordStorage(newPlayer)).copyFrom(oldPlayer);
    }
}
