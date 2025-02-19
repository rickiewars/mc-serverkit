package com.rwconnected.serverkit;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({
    "com.rwconnected.serverkit.util",
//    "com.rwconnected.serverkit.module.http"
})
public class ServerKitTest {
}
