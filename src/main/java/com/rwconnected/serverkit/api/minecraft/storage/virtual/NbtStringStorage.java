package com.rwconnected.serverkit.api.minecraft.storage.virtual;

import com.rwconnected.serverkit.util.IEntityDataSaver;

public class NbtStringStorage extends NbtStorage<String> {
    protected NbtStringStorage(IEntityDataSaver entityDataSaver, String key, String defaultValue) {
        super(entityDataSaver, key, defaultValue);
    }

    @Override
    public String get() {
        return this.entity.serverkit_getPersistentData().getString(this.key, this.defaultValue);
    }

    @Override
    public void set(String value) {
        this.entity.serverkit_getPersistentData().putString(this.key, value);
    }
}
