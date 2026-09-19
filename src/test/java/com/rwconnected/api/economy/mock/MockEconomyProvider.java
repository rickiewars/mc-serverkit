package com.rwconnected.api.economy.mock;

import com.rwconnected.serverkit.api.economy.EconomyProvider;
import com.rwconnected.serverkit.api.economy.ICurrency;
import net.minecraft.resources.Identifier;

import java.util.List;

public class MockEconomyProvider extends EconomyProvider {
    private final List<MockCurrency> currencies;

    public MockEconomyProvider(List<MockCurrency> currencies) {
        this.currencies = currencies;
    }
    public MockEconomyProvider(MockCurrency currency) {
        this.currencies = List.of(currency);
    }
    public MockEconomyProvider() {
        this(new MockCurrency());
    }

    public void addCurrency(MockCurrency currency) {
        this.currencies.add(currency);
    }

    @Override
    protected ICurrency getCurrencyImpl(Identifier currencyId) {
        for (ICurrency currency : currencies) {
            if (currency.getId().equals(currencyId)) {
                return currency;
            }
        }
        return null;
    }
}
