package com.rwconnected.serverkit.util;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.ParseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplatingEngine {
    private static final String DEFAULT_SIMPLE_FORMAT = ".0f";
    private static final String DEFAULT_DECIMAL_FORMAT = ".2f";
    
    // Patterns to match different placeholder formats
    // {expression; singular form; plural form} (e.g. {apples; one apple; $d apples})
    private static final Pattern EXPRESSION_WITH_FORMS_PATTERN = Pattern.compile("\\{([^;\\}]+);([^;\\}]+);([^\\}]+)\\}");
    // {expression; format} (e.g. {pi; %f})
    private static final Pattern EXPRESSION_WITH_FORMAT_PATTERN = Pattern.compile("\\{([^;\\}]+);([^\\}]+)\\}");
    // {expression} (e.g. {days})
    private static final Pattern SIMPLE_EXPRESSION_PATTERN = Pattern.compile("\\{([^;\\}]+)\\}");
    // Pattern to match format specifiers
    private static final Pattern FORMAT_SPECIFIER_PATTERN = Pattern.compile("%[0-9]*\\.?[0-9]*[a-zA-Z]");

    /**
     * Processes the input template by evaluating expressions and replacing placeholders.
     *
     * @param template  The input template string containing placeholders.
     * @param variables A map of variable names and their corresponding values.
     * @return The processed string with placeholders replaced by evaluated results.
     */
    public static String processTemplate(String template, Map<String, BigDecimal> variables) {
        // First, preprocess format specifiers to ensure they are valid
        template = preprocessFormatSpecifiers(template);
        template = preprocessSimpleExpressions(template);

        // Then, process placeholders with singular and plural forms
        template = processExpressionWithForms(template, variables);

        // Then, process placeholders with format specifiers
        template = processExpressionWithFormat(template, variables);

        return template;
    }

    private static String preprocessFormatSpecifiers(String format) {
        return format
            .replaceAll("%[di]", "%" + DEFAULT_SIMPLE_FORMAT)
            .replaceAll("%f(?!\\d)", "%" + DEFAULT_DECIMAL_FORMAT);
    }

    private static String preprocessSimpleExpressions(String template) {
        // Convert any {expression} without semicolons to {expression; %.2f}
        Matcher matcher = SIMPLE_EXPRESSION_PATTERN.matcher(template);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String expression = matcher.group(1).trim();
            String replacement = "{" + expression + "; %" + DEFAULT_SIMPLE_FORMAT + "}";
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private static String processExpressionWithForms(String template, Map<String, BigDecimal> variables) {
        Matcher matcher = EXPRESSION_WITH_FORMS_PATTERN.matcher(template);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String expressionStr = matcher.group(1).trim();
            String singularForm = matcher.group(2).trim();
            String pluralForm = matcher.group(3).trim();

            try {
                BigDecimal value = evaluateExpression(expressionStr, variables);
                boolean isSingular = value.compareTo(BigDecimal.ONE) == 0;
                String chosenForm = isSingular ? singularForm : pluralForm;
                String formattedValue = String.format(chosenForm, value.doubleValue());
                matcher.appendReplacement(result, formattedValue);
            } catch (Exception e) {
                matcher.appendReplacement(result, "[Invalid Expression]");
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private static String processExpressionWithFormat(String template, Map<String, BigDecimal> variables) {
        Matcher matcher = EXPRESSION_WITH_FORMAT_PATTERN.matcher(template);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String expressionStr = matcher.group(1).trim();
            String formatSpecifier = matcher.group(2).trim();

            try {
                BigDecimal value = evaluateExpression(expressionStr, variables);
                String formattedValue = String.format(formatSpecifier, value.doubleValue());
                matcher.appendReplacement(result, formattedValue);
            } catch (Exception e) {
                matcher.appendReplacement(result, "[Invalid Expression]");
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private static BigDecimal evaluateExpression(String expressionStr, Map<String, BigDecimal> variables)
        throws ParseException, EvaluationException {
        Expression expression = new Expression(expressionStr);
        for (Map.Entry<String, BigDecimal> entry : variables.entrySet()) {
            expression.with(entry.getKey(), entry.getValue());
        }
        EvaluationValue evaluationValue = expression.evaluate();
        return evaluationValue.getNumberValue();
    }
}
