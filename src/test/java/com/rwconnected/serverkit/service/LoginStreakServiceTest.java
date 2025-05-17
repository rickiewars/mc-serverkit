package com.rwconnected.serverkit.service;

import com.rwconnected.api.minecraft.player.MockPlayer;
import com.rwconnected.api.util.time.MockTimeProvider;
import com.rwconnected.serverkit.config.Config;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class LoginStreakServiceTest {
    private MockPlayer player;
    private MockTimeProvider timeProvider;
    private LoginStreakService loginStreakService;

    private static final String WELCOME_MESSAGE = "Welcome to the server!";
    private static final String STREAK_LOST_MESSAGE = "Your login streak has been reset.";
    private static final String STREAK_MAINTAINED_MESSAGE = "Your login streak has been maintained.";
    private static final String STREAK_MILESTONE_MESSAGE = "You have reached a milestone!";
    private static final String STREAK_RECURRING_MILESTONE_MESSAGE = "You have reached a periodic milestone!";
    private static final String STREAK_RECURRING_MILESTONE_MESSAGE_2 = "You have reached a second periodic milestone!";

    private static Date getDummyDate() {
        return new GregorianCalendar(2024, Calendar.SEPTEMBER, 10).getTime();
    }

    private void setupService() {
        this.timeProvider = new MockTimeProvider(getDummyDate());
        this.loginStreakService = new LoginStreakService(timeProvider);
    }

    private void initConfig() {
        Config.init(new Config(new Config.LoginStreakConfig(
            List.of(
                new Config.LoginStreakConfig.LoginStreakMilestone(1, 100, STREAK_MAINTAINED_MESSAGE, true),
                new Config.LoginStreakConfig.LoginStreakMilestone(100, 200, STREAK_MILESTONE_MESSAGE, false),
                new Config.LoginStreakConfig.LoginStreakMilestone(7, 300, STREAK_RECURRING_MILESTONE_MESSAGE, true),
                new Config.LoginStreakConfig.LoginStreakMilestone(4*7, 500, STREAK_RECURRING_MILESTONE_MESSAGE_2, true)
            ),
            STREAK_LOST_MESSAGE,
            WELCOME_MESSAGE
        ), null, null));
    }

    private void initPlayer() {
        this.player = new MockPlayer();
        loginStreakService.process(player);
    }

    @Test
    public void testStreakIncrementsAfterNewDay() {
        setupService();
        initPlayer();
        assertEquals(1, player.getLoginStreakStorage().get());

        timeProvider.incrementByDays(1);
        loginStreakService.process(player);
        assertEquals(2, player.getLoginStreakStorage().get());
    }

    @Test
    public void testStreakDoesNotIncrementOnSameDay() {
        setupService();
        initPlayer();
        assertEquals(1, player.getLoginStreakStorage().get());

        loginStreakService.process(player);
        assertEquals(1, player.getLoginStreakStorage().get());
    }

    @Test
    public void testStreakResetsAfterMissingDay() {
        setupService();
        initPlayer();
        this.player.loginStreakStorage.set(3);

        timeProvider.incrementByDays(2);
        loginStreakService.process(player);
        assertEquals(1, player.getLoginStreakStorage().get());
    }

    @Test
    public void testNewPlayersReceivesWelcomeMessage() {
        initConfig();
        setupService();
        this.player = new MockPlayer();
        List<String> receivedMessages = new ArrayList<>();
        player.messageConsumer = (t, message) -> receivedMessages.add(message);

        loginStreakService.process(player);

        assertEquals(1, player.getLoginStreakStorage().get());
        assertEquals(1, receivedMessages.size());
        assertEquals(WELCOME_MESSAGE, receivedMessages.getFirst());
    }

    @Test
    public void testPlayerReceivesStreakMaintainedMessage() {
        initConfig();
        setupService();
        initPlayer();
        List<String> receivedMessages = new ArrayList<>();
        player.messageConsumer = (t, message) -> receivedMessages.add(message);

        timeProvider.incrementByDays(1);
        loginStreakService.process(player);

        assertEquals(1, receivedMessages.size());
        assertEquals(STREAK_MAINTAINED_MESSAGE, receivedMessages.getFirst());
    }

    @Test
    public void testPlayerReceivesStreakLostMessage() {
        initConfig();
        setupService();
        initPlayer();
        this.player.loginStreakStorage.set(3);
        List<String> receivedMessages = new ArrayList<>();
        player.messageConsumer = (t, message) -> receivedMessages.add(message);

        timeProvider.incrementByDays(2);
        loginStreakService.process(player);

        assertEquals(1, receivedMessages.size());
        assertEquals(STREAK_LOST_MESSAGE, receivedMessages.getFirst());
    }

    @Test
    public void testPlayerReceivesMilestoneMessage() {
        initConfig();
        setupService();
        initPlayer();
        this.player.loginStreakStorage.set(99);
        List<String> receivedMessages = new ArrayList<>();
        player.messageConsumer = (t, message) -> receivedMessages.add(message);

        timeProvider.incrementByDays(1);
        loginStreakService.process(player);

        assertEquals(2, receivedMessages.size());
        assertEquals(STREAK_MAINTAINED_MESSAGE, receivedMessages.getFirst());
        assertEquals(STREAK_MILESTONE_MESSAGE, receivedMessages.getLast());
    }

    @Test
    public void testPlayerReceivesRecurringMilestoneMessage() {
        initConfig();
        setupService();
        initPlayer();
        this.player.loginStreakStorage.set(6);
        List<String> receivedMessages = new ArrayList<>();
        player.messageConsumer = (t, message) -> receivedMessages.add(message);

        timeProvider.incrementByDays(1);
        loginStreakService.process(player);

        assertEquals(2, receivedMessages.size());
        assertEquals(STREAK_MAINTAINED_MESSAGE, receivedMessages.getFirst());
        assertEquals(STREAK_RECURRING_MILESTONE_MESSAGE, receivedMessages.getLast());

        receivedMessages.clear();
        this.player.loginStreakStorage.set(13);
        timeProvider.incrementByDays(1);
        loginStreakService.process(player);

        assertEquals(2, receivedMessages.size());
        assertEquals(STREAK_MAINTAINED_MESSAGE, receivedMessages.getFirst());
        assertEquals(STREAK_RECURRING_MILESTONE_MESSAGE, receivedMessages.getLast());
    }

    @Test
    public void testPlayerReceivesMultipleMessagesInOrder() {
        initConfig();
        setupService();
        initPlayer();
        this.player.loginStreakStorage.set(4*7-1);
        List<String> receivedMessages = new ArrayList<>();
        player.messageConsumer = (t, message) -> receivedMessages.add(message);

        timeProvider.incrementByDays(1);
        loginStreakService.process(player);

        assertEquals(3, receivedMessages.size());
        assertEquals(STREAK_MAINTAINED_MESSAGE, receivedMessages.getFirst());
        assertEquals(STREAK_RECURRING_MILESTONE_MESSAGE, receivedMessages.get(1));
        assertEquals(STREAK_RECURRING_MILESTONE_MESSAGE_2, receivedMessages.getLast());
    }

}
