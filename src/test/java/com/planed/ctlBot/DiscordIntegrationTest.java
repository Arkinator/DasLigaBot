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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.planed.ctlBot.domain.UserFixtures.aDefaultUser;
import static com.planed.ctlBot.domain.UserFixtures.anAdmin;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.NONE, classes = {
        BotBoot.class,
        DiscordIntegrationTest.TestClass.class
})
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
    public void shouldCreateUserWhenAnyCommandIsInvoked() {
        final CommandCall call = aCommandCallFromAuthor().createCommandCall();

        assertThat(userEntityRepository.findOne(call.getAuthor().getDiscordId()), is(nullValue()));
        commandRegistry.fireEvent(call);

        assertThat(userEntityRepository.findOne(call.getAuthor().getDiscordId()), is(not(nullValue())));
    }

    @Test
    public void shouldNotCallWhenUserRightsAreInsufficient() {
        final CommandCall call = aCommandCallFromAuthor()
                .setCommandPhrase(COMMAND_PHRASE_2)
                .createCommandCall();
        commandRegistry.fireEvent(call);

        assertThat(lastCall, is(not(call)));
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
        final CommandCall call = aCommandCallFromAuthor().createCommandCall();
        final int numBefore = userService.findUserAndCreateIfNotFound(call.getAuthor().getDiscordId()).getNumberOfInteractions();
        commandRegistry.fireEvent(call);

        final int numAfter = userService.findUserAndCreateIfNotFound(call.getAuthor().getDiscordId()).getNumberOfInteractions();
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
