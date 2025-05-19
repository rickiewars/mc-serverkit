package com.rwconnected.serverkit.api.economy.Patbox;

import com.rwconnected.serverkit.api.economy.IAccount;
import com.rwconnected.serverkit.api.economy.ITransaction;
import eu.pb4.common.economy.api.EconomyAccount;
import net.minecraft.util.Identifier;

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
    public ITransaction increaseBalance(long amount) {
        return new PbTransaction(
            account.increaseBalance(amount)
        );
    }

    @Override
    public ITransaction decreaseBalance(long amount) {
        return new PbTransaction(
            account.decreaseBalance(amount)
        );
    }

    @Override
    public long getBalance() {
        return account.balance();
    }
}
