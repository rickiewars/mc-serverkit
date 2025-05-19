package com.rwconnected.api.economy.mock;

import com.mojang.brigadier.Message;
import com.rwconnected.serverkit.api.economy.ITransaction;
import net.minecraft.text.Text;

public class MockTransaction implements ITransaction {
    public static final String MESSAGE_SUCCESS = "Transaction successful";
    public static final String MESSAGE_UNSUFFICIENT_FUNDS = "Insufficient funds";

    private final boolean result;
    private final String message;

    public MockTransaction(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    @Override
    public boolean isFailure() {
        return !result;
    }

    @Override
    public boolean isSuccess() {
        return result;
    }

    @Override
    public Message getMessage() {
        return Text.of(message);
    }
}
