package com.rwconnected.serverkit.api.economy.Patbox;

import com.rwconnected.serverkit.api.economy.IAccount;
import com.rwconnected.serverkit.api.economy.ITransaction;
import eu.pb4.common.economy.api.EconomyAccount;
import net.minecraft.resources.Identifier;

import java.math.BigInteger;

public class PbAccount implements IAccount {
    private final EconomyAccount account;

    public PbAccount(EconomyAccount account) {
        this.account = account;
    }

    @Override
    public Identifier getId() {
        return account.id();
    }

    @Override
    public ITransaction increaseBalance(BigInteger amount) {
        return new PbTransaction(
            account.increaseBalance(amount)
        );
    }

    @Override
    public ITransaction decreaseBalance(BigInteger amount) {
        return new PbTransaction(
            account.decreaseBalance(amount)
        );
    }

    @Override
    public BigInteger getBalance() {
        return account.balance();
    }
}
