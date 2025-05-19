package com.rwconnected.serverkit.service;

import com.rwconnected.api.economy.mock.MockAccount;
import com.rwconnected.api.economy.mock.MockCurrency;
import com.rwconnected.api.economy.mock.MockEconomyProvider;
import com.rwconnected.api.minecraft.player.MockPlayer;
import com.rwconnected.api.util.time.MockTimeProvider;
import com.rwconnected.serverkit.api.economy.IAccount;
import com.rwconnected.serverkit.config.Config;
import net.minecraft.util.Identifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class LoginStreakServiceTest {
    private MockPlayer player;
    private MockTimeProvider timeProvider;
    private MockEconomyProvider economyProvider;
    private LoginStreakService loginStreakService;

    private static final String WELCOME_MESSAGE = "Welcome to the server!";
    private static final String STREAK_LOST_MESSAGE = "Your login streak has been reset.";
    private static final String STREAK_MAINTAINED_MESSAGE = "Your login streak has been maintained.";
    private static final String STREAK_MILESTONE_MESSAGE = "You have reached a milestone!";
    private static final String STREAK_RECURRING_MILESTONE_MESSAGE = "You have reached a periodic milestone!";
    private static final String STREAK_RECURRING_MILESTONE_MESSAGE_2 = "You have reached a second periodic milestone!";

    private static final int DAILY_REWARD = 100;
    private static final int WEEKLY_REWARD = 300;
    private static final int MONTHLY_REWARD = 500;
    private static final int DAY_100_REWARD = 1000;

    private static Date getDummyDate() {
        return new GregorianCalendar(2024, Calendar.SEPTEMBER, 10).getTime();
    }

    private void setupService() {
        this.timeProvider = new MockTimeProvider(getDummyDate());
        this.economyProvider = new MockEconomyProvider();
        this.loginStreakService = new LoginStreakService(timeProvider, economyProvider);
    }

    private void initConfig(
        Config.EconomyConfig economyConfig
    ) {
        Config.init(new Config(
            new Config.LoginStreakConfig(
                List.of(
                    new Config.LoginStreakConfig.LoginStreakMilestone(1, DAILY_REWARD, STREAK_MAINTAINED_MESSAGE, true),
                    new Config.LoginStreakConfig.LoginStreakMilestone(100, DAY_100_REWARD, STREAK_MILESTONE_MESSAGE, false),
                    new Config.LoginStreakConfig.LoginStreakMilestone(7, WEEKLY_REWARD, STREAK_RECURRING_MILESTONE_MESSAGE, true),
                    new Config.LoginStreakConfig.LoginStreakMilestone(4*7, MONTHLY_REWARD, STREAK_RECURRING_MILESTONE_MESSAGE_2, true)
                ),
                STREAK_LOST_MESSAGE,
                WELCOME_MESSAGE
            ),
            null,
            null,
            economyConfig != null ? economyConfig : new Config.EconomyConfig(
                Identifier.of("mock", "currency"),
                Identifier.of("mock", "account")
            )
        ));
    }

    private void initConfig() {
        initConfig(null);
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

    @Test
    public void testPlayerGetsRewardedForStreak() {
        initConfig();
        setupService();
        initPlayer();

        timeProvider.incrementByDays(1);
        loginStreakService.process(player);

        IAccount account = this.economyProvider.getDefaultAccount(
            this.player, Config.instance().economy.currencyId()
        );

        assertEquals(DAILY_REWARD, account.getBalance());
    }

    @Test
    public void testPlayerGetsNoRewardAfterMissingDay() {
        initConfig();
        setupService();
        initPlayer();
        this.player.loginStreakStorage.set(3);

        timeProvider.incrementByDays(2);
        loginStreakService.process(player);

        IAccount account = this.economyProvider.getDefaultAccount(
            this.player, Config.instance().economy.currencyId()
        );

        assertEquals(0, account.getBalance());
    }

    @Test
    public void testPlayerGetsRewardForWeeklyMilestone() {
        initConfig();
        setupService();
        initPlayer();
        this.player.loginStreakStorage.set(6);

        timeProvider.incrementByDays(1);
        loginStreakService.process(player);

        IAccount account = this.economyProvider.getDefaultAccount(
            this.player, Config.instance().economy.currencyId()
        );

        assertEquals(DAILY_REWARD + WEEKLY_REWARD, account.getBalance());
    }

    @Test
    public void testPlayerGetsRewardForMultipleMilestones() {
        initConfig();
        setupService();
        initPlayer();
        this.player.loginStreakStorage.set(4*7-1);

        timeProvider.incrementByDays(1);
        loginStreakService.process(player);

        IAccount account = this.economyProvider.getDefaultAccount(
            this.player, Config.instance().economy.currencyId()
        );

        assertEquals(DAILY_REWARD + WEEKLY_REWARD + MONTHLY_REWARD, account.getBalance());
    }

    @Test
    public void testPlayerGetsRewardForRegularMilestone() {
        initConfig();
        setupService();
        initPlayer();
        this.player.loginStreakStorage.set(99);

        timeProvider.incrementByDays(1);
        loginStreakService.process(player);

        IAccount account = this.economyProvider.getDefaultAccount(
            this.player, Config.instance().economy.currencyId()
        );

        assertEquals(DAILY_REWARD + DAY_100_REWARD, account.getBalance());
    }

    @Test
    public void testPlayerGetsNoRewardForAlreadyReceivedRegularMilestone() {
        initConfig();
        setupService();
        initPlayer();
        this.player.loginStreakRecordStorage.set(101);
        this.player.loginStreakStorage.set(99);

        timeProvider.incrementByDays(1);
        loginStreakService.process(player);

        IAccount account = this.economyProvider.getDefaultAccount(
            this.player, Config.instance().economy.currencyId()
        );

        assertEquals(DAILY_REWARD, account.getBalance());
    }

    @Test
    public void testLoginStreakServiceStillWorksWithMisconfiguredEconomyConfig() {
        initConfig(new Config.EconomyConfig(
            Identifier.of("invalid", "id1"),
            Identifier.of("invalid", "id2")
        ));
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

}
