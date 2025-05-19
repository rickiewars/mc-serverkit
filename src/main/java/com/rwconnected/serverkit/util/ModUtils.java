package com.rwconnected.serverkit.util;

import com.rwconnected.serverkit.ServerKit;
import com.rwconnected.serverkit.config.Config;

import java.math.BigDecimal;
import java.util.Map;

public class ModUtils {

    public static String formatCurrency(int amount) {
        String format = Config.instance().templatingEngine.pipelines().get(
            Config.TemplatingEngineConfig.PIPELINE_KEY_CURRENCY
        );

        try {
            return TemplatingEngine.processTemplate(format, Map.of("input", BigDecimal.valueOf(amount)));
        } catch (Exception e) {
            ServerKit.LOGGER.error("Failed to parse reward template: {}", format, e);
            return format;
        }
    }
}
