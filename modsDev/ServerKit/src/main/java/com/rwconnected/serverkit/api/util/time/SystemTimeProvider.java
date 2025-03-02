package com.rwconnected.serverkit.api.util.time;

import java.util.Calendar;
import java.util.Date;

public class SystemTimeProvider implements ITimeProvider {

    protected Calendar calendar() {
        return Calendar.getInstance();
    }

    @Override
    public Date now() {
        return calendar().getTime();
    }

    @Override
    public Date yesterday() {
        return plusDays(-1);
    }

    @Override
    public Date tomorrow() {
        return plusDays(1);
    }

    @Override
    public Date plusDays(int days) {
        Calendar cal = calendar();
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    @Override
    public Date plusWeeks(int weeks) {
        return plusDays(weeks * 7);
    }

    @Override
    public Date plusMonths(int months) {
        Calendar cal = calendar();
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    @Override
    public Date plusYears(int years) {
        Calendar cal = calendar();
        cal.add(Calendar.YEAR, years);
        return cal.getTime();
    }
}
