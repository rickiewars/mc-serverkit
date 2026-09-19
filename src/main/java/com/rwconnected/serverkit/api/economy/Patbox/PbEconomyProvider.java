package com.rwconnected.serverkit.api.economy.Patbox;

import com.rwconnected.serverkit.api.economy.EconomyProvider;
import com.rwconnected.serverkit.api.economy.ICurrency;
import eu.pb4.common.economy.api.CommonEconomy;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;

public class PbEconomyProvider extends EconomyProvider {
    MinecraftServer server;

    public PbEconomyProvider(MinecraftServer server) {
        this.server = server;
    }

    @Override
    protected ICurrency getCurrencyImpl(Identifier currencyId)
    {
        return new PbCurrency(CommonEconomy.getCurrency(server, currencyId));
    }
}
