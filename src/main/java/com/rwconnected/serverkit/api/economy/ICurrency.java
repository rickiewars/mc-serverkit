package com.rwconnected.serverkit.api.economy;

import com.rwconnected.serverkit.api.minecraft.player.IPlayer;
import net.minecraft.util.Identifier;

public interface ICurrency {

    public Identifier getId();
    public IAccount getDefaultAccount(IPlayer<?> player);
}
