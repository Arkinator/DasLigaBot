package com.planed.ctlBot;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.commands.data.CommandCallBuilder;
import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.data.repositories.UserEntityRepository;
import com.planed.ctlBot.discord.CommandRegistry;
import com.planed.ctlBot.discord.DiscordCommand;
import com.planed.ctlBot.discord.DiscordController;
import com.planed.ctlBot.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.planed.ctlBot.domain.UserFixtures.aDefaultUser;
import static com.planed.ctlBot.domain.UserFixtures.anAdmin;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Julian Peters on 24.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@SpringApplicationConfiguration(classes = {
        BotBoot.class,
        DiscordIntegrationTest.TestClass.class
})
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("development")
public class DiscordIntegrationTest {
    private static final String AUTHOR_ID = "author";
    private static final String ADMIN_ID = "admin";
    private static final String COMMAND_PHRASE = "command1";
    private static final String COMMAND_PHRASE_2 = "command2";
    @Autowired
    private CommandRegistry commandRegistry;
    @Autowired
    private UserService userService;
    @Autowired
    private UserEntityRepository userEntityRepository;
    private static CommandCall lastCall;

    @Before
    public void setUp() {
        userEntityRepository.deleteAll();
        lastCall = null;

        userService.findUserAndCreateIfNotFound(ADMIN_ID);
        userService.giveUserAccessLevel(ADMIN_ID, AccessLevel.Admin);
    }

    @Test
    public void shouldCreateUserWhenCommandIsInvoked() {
        assertThat(userEntityRepository.findByDiscordId(AUTHOR_ID), is(nullValue()));

        final CommandCall call = aCommandCallFromAuthor().createCommandCall();
        commandRegistry.fireEvent(call);

        assertThat(userEntityRepository.findByDiscordId(AUTHOR_ID), is(not(nullValue())));
    }

    @Test(expected = CommandRegistry.InsufficientAccessRightsException.class)
    public void shouldNotCallWhenUserRightsAreInsufficient() {
        final CommandCall call = aCommandCallFromAuthor()
                .setCommandPhrase(COMMAND_PHRASE_2)
                .createCommandCall();
        commandRegistry.fireEvent(call);
    }

    @Test
    public void shouldCallWhenUserRightsAreSufficient() {
        final CommandCall call = aCommandCallFromAuthor()
                .setAuthor(anAdmin())
                .setCommandPhrase(COMMAND_PHRASE_2)
                .createCommandCall();
        commandRegistry.fireEvent(call);

        assertThat(lastCall, is(call));
    }

    @Test
    public void shouldIncrementNumberOfUserInteractionsWhenInvoked() {
        final int numBefore = userService.findUserAndCreateIfNotFound(AUTHOR_ID).getNumberOfInteractions();
        final CommandCall call = aCommandCallFromAuthor().createCommandCall();
        commandRegistry.fireEvent(call);

        final int numAfter = userService.findUserAndCreateIfNotFound(AUTHOR_ID).getNumberOfInteractions();
        assertThat(numAfter - numBefore, is(1));
    }

    private CommandCallBuilder aCommandCallFromAuthor() {
        return new CommandCallBuilder()
                .setAuthor(aDefaultUser())
                .setCommandPhrase(COMMAND_PHRASE);
    }

    @DiscordController
    public static class TestClass {
        @DiscordCommand(name = COMMAND_PHRASE)
        public void testCommand1(final CommandCall call) {
            lastCall = call;
        }

        @DiscordCommand(name = COMMAND_PHRASE_2, roleRequired = AccessLevel.Admin)
        public void testCommand2(final CommandCall call) {
            lastCall = call;
        }
    }
}
