package com.rwconnected.serverkit.api.minecraft.storage.virtual;

public interface INumericStorage<T> extends IStorage<T> {
    public void increment();
    public void decrement();
}
