package com.rwconnected.api.economy.mock;

import com.rwconnected.serverkit.api.economy.IAccount;
import com.rwconnected.serverkit.api.economy.ITransaction;
import net.minecraft.resources.Identifier;

import java.math.BigInteger;

public class MockAccount implements IAccount {
    private final Identifier id;
    private BigInteger balance;

    public MockAccount(Identifier id, long startingBalance) {
        this.id = id;
        this.balance = BigInteger.valueOf(startingBalance);
    }
    public MockAccount(long startingBalance) {
        this(Identifier.fromNamespaceAndPath("mock", "account"), startingBalance);
    }
    public MockAccount() {
        this(0);
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public ITransaction increaseBalance(BigInteger amount) {
        this.balance = this.balance.add(amount);
        return new MockTransaction(true, MockTransaction.MESSAGE_SUCCESS);
    }

    @Override
    public ITransaction decreaseBalance(BigInteger amount) {
        if (this.balance.compareTo(amount) < 0) {
            return new MockTransaction(false, MockTransaction.MESSAGE_UNSUFFICIENT_FUNDS);
        }

        this.balance = this.balance.subtract(amount);
        return new MockTransaction(true, MockTransaction.MESSAGE_SUCCESS);
    }

    @Override
    public BigInteger getBalance() {
        return balance;
    }
}
