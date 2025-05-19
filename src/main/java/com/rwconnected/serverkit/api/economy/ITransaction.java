package com.rwconnected.serverkit.api.economy;

import com.mojang.brigadier.Message;

public interface ITransaction {
    public boolean isFailure();
    public boolean isSuccess();
    public Message getMessage();
}
