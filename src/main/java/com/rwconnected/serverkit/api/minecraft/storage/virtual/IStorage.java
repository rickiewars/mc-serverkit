package com.rwconnected.serverkit.api.minecraft.storage.virtual;


public interface IStorage<T> {
    public T get();
    public void set(T value);
    public void reset();
}
