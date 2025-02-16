package net.rickiewars.datetime.util;

import net.minecraft.nbt.NbtCompound;

public interface IEntityDataSaver {
    NbtCompound datetime_getPersistentData();
}