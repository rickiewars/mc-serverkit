package com.rwconnected.serverkit.config;

import com.rwconnected.serverkit.ServerKit;
import com.rwconnected.serverkit.util.TemplatingEngine;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    public static final String FILE_NAME = "rwconnected/serverkit.json";

    private static Config instance;

    public LoginStreakConfig loginStreak;
    public HttpServerConfig httpServer;

    public Config(LoginStreakConfig loginStreakConfig, HttpServerConfig httpServerConfig) {
        this.loginStreak = loginStreakConfig != null ? loginStreakConfig : new LoginStreakConfig();
        this.httpServer = httpServerConfig != null ? httpServerConfig : new HttpServerConfig();
    }

    /**
     * Initializes the default configuration
     */
    public Config() {
        this(new LoginStreakConfig(), new HttpServerConfig());
    }

    public static Config instance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }
    public static void init(@NotNull Config config) {
        instance = config;
    }

    public record LoginStreakConfig(
        List<LoginStreakMilestone> milestones
    ){
        public LoginStreakConfig(List<LoginStreakMilestone> milestones) {
            this.milestones = milestones != null ? milestones : List.of();
        }
        public LoginStreakConfig() {
            this(List.of(
                new LoginStreakMilestone(1, 100,
                    "Welcome back! You've maintained a login streak for {streak; one day; %d days}! Here's a reward of {reward/100; %.2f} credits."
                    , true),
                new LoginStreakMilestone(7, 200,
                    "You've managed to login every day for {streak/7; a whole week; %d weeks}! That means you've earned an additional reward of {reward/100; %.2f} credits.",
                    true),
                new LoginStreakMilestone(30, 1000,
                    "You've been consistently logging in for {streak/30; a whole month; %d months}! To show our appreciation, here's an additional reward of {reward/100; %.2f} credits.",
                    true),
                new LoginStreakMilestone(365, 5000,
                    "Congratulations on maintaining a login streak for {streak/365; a whole year; %d years}! For this amazing achievement, we're rewarding you with {reward/100; %.2f} credits.",
                    true)
            ));
        }
        public record LoginStreakMilestone(int days, int reward, String message, boolean periodic){

            // TODO: Make message an array so that a message can have multiple lines
            // Idea: an array of messages which can be randomly selected.

            public String parseMessage(int streak) {
                return parseMessage(streak, Map.of());
            }
            public String parseMessage(int streak, Map<String, BigDecimal> additionalVariables) {
                try {
                    Map<String, BigDecimal> variables = new HashMap<>(Map.of(
                        "streak", BigDecimal.valueOf(streak),
                        "reward", BigDecimal.valueOf(reward)
                    ));
                    variables.putAll(additionalVariables);

                    return TemplatingEngine.processTemplate(message, variables);
                } catch (Exception e) {
                    ServerKit.LOGGER.error("Failed to parse message template: " + message, e);
                    return message;
                }
            }
        }


    }

    public record HttpServerConfig(
        String host,
        int port
    ){
        public HttpServerConfig(String host, int port) {
            this.host = host;
            this.port = port;
        }
        public HttpServerConfig() {
            this("0.0.0.0", 8080);
        }
    }

}
