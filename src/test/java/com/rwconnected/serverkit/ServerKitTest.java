package com.rwconnected.serverkit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({
    "com.rwconnected.serverkit.util",
    "com.rwconnected.serverkit.service",
})
public class ServerKitTest {
    @BeforeAll
    static void setup() {
    }
}
