package com.planed.ctlBot;

import com.planed.ctlBot.commands.data.DiscordMessage;
import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.data.repositories.UserRepository;
import com.planed.ctlBot.discord.CommandRegistry;
import com.planed.ctlBot.discord.DiscordCommand;
import com.planed.ctlBot.discord.DiscordController;
import com.planed.ctlBot.services.UserService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.planed.ctlBot.domain.UserFixtures.aDefaultUser;
import static com.planed.ctlBot.domain.UserFixtures.anAdmin;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {
        BotBoot.class,
        DiscordIntegrationTest.TestClass.class},
        properties = {"spring.flyway.enabled: false", "flyway.enabled: false", "spring.jpa.hibernate.ddl-auto: create-drop"})
@Ignore("Client-Test")
public class DiscordIntegrationTest {
    private static final String AUTHOR_ID = "author";
    private static final String ADMIN_ID = "admin";
    private static final String COMMAND_PHRASE = "command1";
    private static final String COMMAND_PHRASE_2 = "command2";
    private static DiscordMessage lastCall;
    @Autowired
    private CommandRegistry commandRegistry;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        userRepository.deleteAll();
        lastCall = null;

        userService.findUserAndCreateIfNotFound(ADMIN_ID);
        userService.giveUserAccessLevel(ADMIN_ID, AccessLevel.ADMIN);
    }

    @Test
    public void shouldCreateUserWhenAnyCommandIsInvoked() {
        final DiscordMessage call = aDiscordMessageFromAuthor().build();

        assertThat(userRepository.findById(call.getAuthor().getDiscordId()).isPresent(), is(false));
        commandRegistry.fireEvent(call);

        assertThat(userRepository.findById(call.getAuthor().getDiscordId()).get(), is(not(nullValue())));
    }

    @Test
    public void shouldNotCallWhenUserRightsAreInsufficient() {
        final DiscordMessage call = aDiscordMessageFromAuthor()
                .commandPhrase(COMMAND_PHRASE_2)
                .build();
        commandRegistry.fireEvent(call);

        assertThat(lastCall, is(not(call)));
    }

    @Test
    public void shouldCallWhenUserRightsAreSufficient() {
        final DiscordMessage call = aDiscordMessageFromAuthor()
                .author(anAdmin())
                .commandPhrase(COMMAND_PHRASE_2)
                .build();
        commandRegistry.fireEvent(call);

        assertThat(lastCall, is(call));
    }

    @Test
    public void shouldIncrementNumberOfUserInteractionsWhenInvoked() {
        final DiscordMessage call = aDiscordMessageFromAuthor().build();
        final int numBefore = userService.findUserAndCreateIfNotFound(call.getAuthor().getDiscordId()).getNumberOfInteractions();
        commandRegistry.fireEvent(call);

        final int numAfter = userService.findUserAndCreateIfNotFound(call.getAuthor().getDiscordId()).getNumberOfInteractions();
        assertThat(numAfter - numBefore, is(1));
    }

    private DiscordMessage.DiscordMessageBuilder aDiscordMessageFromAuthor() {
        return DiscordMessage.builder()
                .author(aDefaultUser())
                .commandPhrase(COMMAND_PHRASE);
    }

    @DiscordController
    public static class TestClass {
        @DiscordCommand(name = COMMAND_PHRASE)
        public void testCommand1(final DiscordMessage call) {
            lastCall = call;
        }

        @DiscordCommand(name = COMMAND_PHRASE_2, roleRequired = AccessLevel.ADMIN)
        public void testCommand2(final DiscordMessage call) {
            lastCall = call;
        }
    }
}
