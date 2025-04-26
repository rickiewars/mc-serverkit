package com.rwconnected.serverkit.api.minecraft.storage.virtual;

import com.rwconnected.serverkit.util.IEntityDataSaver;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class NbtStorage<T> implements IStorage<T> {
    protected final IEntityDataSaver entity;
    protected final String key;
    protected final T defaultValue;

    protected NbtStorage(IEntityDataSaver entityDataSaver, String key, T defaultValue) {
        this.entity = entityDataSaver;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Override
    public void remove() {
        entity.serverkit_getPersistentData().remove(this.key);
    }

    @Override
    public void reset() {
        set(defaultValue);
    }

    public void copyFrom(NbtStorage<T> storage) {
        set(storage.get());
    }

    public void copyFrom(IEntityDataSaver entityDataSaver) {
        NbtElement value = entityDataSaver.serverkit_getPersistentData().get(this.key);
        entity.serverkit_getPersistentData().put(this.key, value);
    }

    public void copyFrom(ServerPlayerEntity player) {
        copyFrom((IEntityDataSaver) player);
    }
}
