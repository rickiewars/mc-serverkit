package com.rwconnected.serverkit.api.minecraft.storage.virtual;

import com.rwconnected.serverkit.util.IEntityDataSaver;

public class NbtIntStorage extends NbtStorage<Integer> implements INumericStorage<Integer> {
    protected NbtIntStorage(IEntityDataSaver entityDataSaver, String key, int defaultValue) {
        super(entityDataSaver, key, defaultValue);
    }

    @Override
    public Integer get() {
        return this.entity.serverkit_getPersistentData().getInt(this.key, this.defaultValue);
    }

    @Override
    public void set(Integer value) {
        this.entity.serverkit_getPersistentData().putInt(this.key, value);
    }

    @Override
    public void increment() {
        int value = get() + 1;
        set(value);
    }

    @Override
    public void decrement() {
        int value = get() - 1;
        set(value);
    }
}
