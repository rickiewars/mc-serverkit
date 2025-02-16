package com.rwconnected.serverkit.event;


import com.rwconnected.serverkit.storage.LastLoginDate;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import com.rwconnected.serverkit.util.IEntityDataSaver;

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
        IEntityDataSaver oldData = (IEntityDataSaver) oldPlayer;
        IEntityDataSaver newData = (IEntityDataSaver) newPlayer;

        LastLoginDate.set(newData, LastLoginDate.get(oldData));
        newData.serverkit_getPersistentData().putInt("loginStreak", oldData.serverkit_getPersistentData().getInt("loginStreak"));
        newData.serverkit_getPersistentData().putInt("bestLoginStreak", oldData.serverkit_getPersistentData().getInt("bestLoginStreak"));
    }
}
