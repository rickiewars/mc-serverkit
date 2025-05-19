package com.rwconnected.serverkit.api.economy.Patbox;

import com.rwconnected.serverkit.api.economy.IAccount;
import com.rwconnected.serverkit.api.economy.ICurrency;
import com.rwconnected.serverkit.api.minecraft.player.IPlayer;
import com.rwconnected.serverkit.api.minecraft.player.Player;
import eu.pb4.common.economy.api.EconomyCurrency;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PbCurrency implements ICurrency {
    private final EconomyCurrency currency;

    public PbCurrency(EconomyCurrency currency) {
        this.currency = currency;
    }

    @Override
    public Identifier getId() {
        return currency.id();
    }

    @Override
    public IAccount getDefaultAccount(IPlayer<?> player) {
        ServerPlayerEntity mcPlayer = ((Player) player).getSource();
        return new PbAccount(currency.provider().getDefaultAccount(mcPlayer, currency));
    }
}
