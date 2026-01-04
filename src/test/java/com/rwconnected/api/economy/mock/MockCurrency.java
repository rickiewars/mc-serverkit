package com.rwconnected.api.economy.mock;

import com.rwconnected.serverkit.api.economy.IAccount;
import com.rwconnected.serverkit.api.economy.ICurrency;
import com.rwconnected.serverkit.api.minecraft.player.IPlayer;
import net.minecraft.util.Identifier;

public class MockCurrency implements ICurrency {
    private final Identifier id;
    private final MockAccount defaultAccount;

    public String prefix = "";
    public String suffix = " RP";
    public int decimalPlaces = 2;

    public MockCurrency(Identifier id, MockAccount defaultAccount) {
        this.id = id;
        this.defaultAccount = defaultAccount;
    }
    public MockCurrency(MockAccount defaultAccount) {
        this(Identifier.of("mock", "currency"), defaultAccount);
    }
    public MockCurrency() {
        this(new MockAccount());
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public IAccount getDefaultAccount(IPlayer<?> player) {
        return defaultAccount;
    }

    @Override
    public String formatValue(long value) {
        int fraction = (int) (value % (long) Math.pow(10, decimalPlaces));
        long whole = value / (long) Math.pow(10, decimalPlaces);
        return String.format("%s%d.%0" + decimalPlaces + "d%s", prefix, whole, fraction, suffix);
    }
}
