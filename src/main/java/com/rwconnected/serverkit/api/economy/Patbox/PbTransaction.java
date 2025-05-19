package com.rwconnected.serverkit.api.economy.Patbox;

import com.mojang.brigadier.Message;
import com.rwconnected.serverkit.api.economy.ITransaction;
import eu.pb4.common.economy.api.EconomyTransaction;

public class PbTransaction implements ITransaction {
    private final EconomyTransaction transaction;

    public PbTransaction(EconomyTransaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public boolean isFailure() {
        return transaction.isFailure();
    }

    @Override
    public boolean isSuccess() {
        return transaction.isSuccessful();
    }

    @Override
    public Message getMessage() {
        return transaction.message();
    }
}
