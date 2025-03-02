package com.rwconnected.serverkit.util;

import net.minecraft.nbt.NbtCompound;

public interface IEntityDataSaver {
    NbtCompound serverkit_getPersistentData();
}