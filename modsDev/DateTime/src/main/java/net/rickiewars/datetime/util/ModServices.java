package net.rickiewars.datetime.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ModServices {
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

    public static void register() {
//        schedule(HttpModule::syncPlayerStatistics, 30);
    }

    private static void schedule(Runnable runnable, long period_s) {
        schedule(runnable, period_s, 0);
    }
    private static void schedule(Runnable runnable, long period_s, long delay_s) {
        executorService.scheduleWithFixedDelay(runnable, delay_s, period_s, TimeUnit.SECONDS);
    }
}
