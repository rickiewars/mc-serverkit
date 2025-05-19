package com.rwconnected.serverkit.api.economy;

import net.minecraft.util.Identifier;

public interface IAccount {
    public Identifier getId();
    public ITransaction increaseBalance(long amount);
    public ITransaction decreaseBalance(long amount);
    public long getBalance();
}
