package com.rwconnected.serverkit.api.economy;

import com.rwconnected.serverkit.api.minecraft.player.IPlayer;
import net.minecraft.util.Identifier;

public abstract class EconomyProvider {
    protected abstract ICurrency getCurrencyImpl(Identifier currencyId);

    public final ICurrency getCurrency(Identifier currencyId) throws IllegalArgumentException {
        ICurrency currency = getCurrencyImpl(currencyId);
        if (currency == null) {
            throw new IllegalArgumentException("Currency not found: " + currencyId);
        }

        return currency;
    }

    public final IAccount getDefaultAccount(IPlayer<?> player, Identifier currencyId) throws IllegalArgumentException {
        ICurrency currency = getCurrency(currencyId);
        IAccount account = currency.getDefaultAccount(player);
        if (account == null) {
            throw new IllegalArgumentException(
                "Default account not found for player: " + player.getName() + " and currency: " + currencyId
            );
        }

        return account;
    }
}
