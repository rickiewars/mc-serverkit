package com.rwconnected.serverkit.api.economy;

import net.minecraft.resources.Identifier;

import java.math.BigInteger;

public interface IAccount {
    public Identifier getId();
    public ITransaction increaseBalance(BigInteger amount);
    public ITransaction decreaseBalance(BigInteger amount);
    public BigInteger getBalance();
}
