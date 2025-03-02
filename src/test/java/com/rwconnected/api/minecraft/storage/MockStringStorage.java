package com.rwconnected.api.minecraft.storage;

import com.rwconnected.serverkit.api.minecraft.storage.virtual.IStorage;

public class MockStringStorage implements IStorage<String> {
    private String value;
    private final String defaultValue;

    public MockStringStorage(String startValue, String defaultValue) {
        this.value = startValue;
        this.defaultValue = defaultValue;
    }

    public MockStringStorage(String startValue) {
        this(startValue, "");
    }
    public MockStringStorage() {
        this("");
    }

    @Override
    public String get() {
        return this.value;
    }

    @Override
    public void set(String value) {
        this.value = value;
    }

    @Override
    public void remove() {
        this.value = "";
    }

    @Override
    public void reset() {
        this.value = this.defaultValue;
    }
}
