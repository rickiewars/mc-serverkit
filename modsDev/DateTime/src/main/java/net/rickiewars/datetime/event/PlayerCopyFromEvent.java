package net.rickiewars.datetime.event;


import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.rickiewars.datetime.storage.LastLoginDate;
import net.rickiewars.datetime.util.IEntityDataSaver;

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
        newData.getPersistentData().putInt("loginStreak", oldData.getPersistentData().getInt("loginStreak"));
        newData.getPersistentData().putInt("bestLoginStreak", oldData.getPersistentData().getInt("bestLoginStreak"));
    }
}
