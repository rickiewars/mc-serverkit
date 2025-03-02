package com.rwconnected.api.minecraft.storage;

import com.rwconnected.serverkit.api.minecraft.storage.virtual.INumericStorage;
import com.rwconnected.serverkit.api.minecraft.storage.virtual.NbtStorage;
import com.rwconnected.serverkit.util.IEntityDataSaver;

public class MockIntStorage implements INumericStorage<Integer> {
    private int value;
    private final int defaultValue;

    public MockIntStorage(int startValue, int defaultValue) {
        this.value = startValue;
        this.defaultValue = defaultValue;
    }

    public MockIntStorage(int startValue) {
        this(startValue, 0);
    }
    public MockIntStorage() {
        this(0);
    }

    @Override
    public Integer get() {
        return this.value;
    }

    @Override
    public void set(Integer value) {
        this.value = value;
    }

    @Override
    public void remove() {
        this.value = 0;
    }

    @Override
    public void reset() {
        this.value = this.defaultValue;
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
