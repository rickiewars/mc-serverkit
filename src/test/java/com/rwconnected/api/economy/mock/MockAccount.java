package com.rwconnected.api.economy.mock;

import com.rwconnected.serverkit.api.economy.IAccount;
import com.rwconnected.serverkit.api.economy.ITransaction;
import net.minecraft.util.Identifier;

public class MockAccount implements IAccount {
    private final Identifier id;
    private long balance;

    public MockAccount(Identifier id, long startingBalance) {
        this.id = id;
        this.balance = startingBalance;
    }
    public MockAccount(long startingBalance) {
        this(Identifier.of("mock", "account"), startingBalance);
    }
    public MockAccount() {
        this(0);
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public ITransaction increaseBalance(long amount) {
        this.balance += amount;
        return new MockTransaction(true, MockTransaction.MESSAGE_SUCCESS);
    }

    @Override
    public ITransaction decreaseBalance(long amount) {
        if (this.balance < amount) {
            return new MockTransaction(false, MockTransaction.MESSAGE_UNSUFFICIENT_FUNDS);
        }

        this.balance -= amount;
        return new MockTransaction(true, MockTransaction.MESSAGE_SUCCESS);
    }

    @Override
    public long getBalance() {
        return balance;
    }
}
