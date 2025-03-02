package com.rwconnected.serverkit.api.util.time;

import java.util.Date;

public interface ITimeProvider {
    Date now();
    Date yesterday();
    Date tomorrow();
    Date plusDays(int days);
    Date plusWeeks(int weeks);
    Date plusMonths(int months);
    Date plusYears(int years);
}
