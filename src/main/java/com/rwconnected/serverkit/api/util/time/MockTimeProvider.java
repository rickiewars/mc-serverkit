package com.rwconnected.serverkit.api.util.time;

import java.util.Calendar;
import java.util.Date;

public class MockTimeProvider extends SystemTimeProvider {
    public Date date;

    public MockTimeProvider(Date date) {
        this.date = date;
    }

    public void incrementByDays(int days) {
        this.date = plusDays(days);
    }

    @Override
    protected Calendar calendar() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
