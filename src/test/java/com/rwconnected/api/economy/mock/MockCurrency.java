package com.rwconnected.api.economy.mock;

import com.rwconnected.serverkit.api.economy.IAccount;
import com.rwconnected.serverkit.api.economy.ICurrency;
import com.rwconnected.serverkit.api.minecraft.player.IPlayer;
import net.minecraft.util.Identifier;

public class MockCurrency implements ICurrency {
    private final Identifier id;
    private final MockAccount defaultAccount;

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
}
