package com.rwconnected.serverkit.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class TemplatingEngineTest {
    @Test
    public void testWithoutExpressions() {
        String template = "Hello, world!";

        String result = TemplatingEngine.processTemplate(template, null);
        assertEquals("Hello, world!", result);
    }

    @Test
    public void testWithSimpleExpression() {
        String template = "I ate, {amount} apples!";
        BigDecimal amount = BigDecimal.valueOf(3);

        String result = TemplatingEngine.processTemplate(template, Map.of("amount", amount));
        assertEquals("I ate, 3 apples!", result);
    }

    @Test
    public void testWithExpressionWithForms() {
        String template = "I have {apples;one apple;%d apples}!";

        BigDecimal apples = BigDecimal.valueOf(3);
        String result = TemplatingEngine.processTemplate(template, Map.of("apples", apples));
        assertEquals("I have 3 apples!", result);

        apples = BigDecimal.valueOf(1);
        result = TemplatingEngine.processTemplate(template, Map.of("apples", apples));
        assertEquals("I have one apple!", result);
    }

    @Test
    public void testWithInvalidExpression() {
        String template = "I have {apples;one apple;%d apples}!";
        BigDecimal apples = BigDecimal.valueOf(-3);

        String result = TemplatingEngine.processTemplate(template, Map.of("pears", apples));
        assertEquals("I have [Invalid Expression]!", result);
    }

    @Test
    public void testWithMultipleExpressions() {
        String template = "I have {apples;one apple;%d apples} and {pears;one pear;%d pears}!";
        BigDecimal apples = BigDecimal.valueOf(3);
        BigDecimal pears = BigDecimal.valueOf(1);

        String result = TemplatingEngine.processTemplate(template, Map.of("apples", apples, "pears", pears));
        assertEquals("I have 3 apples and one pear!", result);
    }

    @Test
    public void testWithMultipleVariedExpressions() {
        String template = "I have {apples} apples and {pears;one pear;%d pears}!";
        BigDecimal apples = BigDecimal.valueOf(2);
        BigDecimal pears = BigDecimal.valueOf(4);

        String result = TemplatingEngine.processTemplate(template, Map.of("apples", apples, "pears", pears));
        assertEquals("I have 2 apples and 4 pears!", result);
    }

    @Test
    public void testWithExpression() {
        String template = "I've been online for about {minutes / 60} hours, or {minutes * 60} seconds.";
        BigDecimal minutes = BigDecimal.valueOf(120);

        String result = TemplatingEngine.processTemplate(template, Map.of("minutes", minutes));
        assertEquals("I've been online for about 2 hours, or 7200 seconds.", result);
    }

    @Test
    public void testFloatingPointExpression() {
        String template = "Pi is approximately {pi;%f} but can be more precisely expressed as {pi;%.3f}.";
        BigDecimal pi = BigDecimal.valueOf(Math.PI);

        String result = TemplatingEngine.processTemplate(template, Map.of("pi", pi));
        assertEquals("Pi is approximately 3.14 but can be more precisely expressed as 3.142.", result);
    }
}
