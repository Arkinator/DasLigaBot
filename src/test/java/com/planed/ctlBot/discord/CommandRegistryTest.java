package com.planed.ctlBot.discord;

/**
 * Created by Julian Peters on 23.04.16.
 *
 * @author julian.peters@westernacher.com
 */

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.services.UserService;
import com.planed.ctlBot.utils.DiscordMessageParser;
import org.javacord.api.DiscordApi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {CommandRegistry.class,
                CommandRegistryTest.TestClass.class})
public class CommandRegistryTest {
    private static List<Object> callParameters;
    @Autowired
    private CommandRegistry commandRegistry;
    @MockBean
    private UserService userService;
    @MockBean
    private DiscordService discordService;
    @MockBean
    private DiscordApi discordApi;
    @MockBean
    private DiscordMessageParser discordMessageParser;
    private String discordId = "discord-id";

    @Before
    public void setUp() {
        doReturn(aStandardUser()).when(userService).findUserAndCreateIfNotFound(Matchers.eq(discordId));
        callParameters = new ArrayList<>();
    }

    private User aStandardUser() {
        User result = new User();
        result.setAccessLevel(AccessLevel.USER);
        result.setDiscordId(discordId);
        return result;
    }

    private User anAdminUser() {
        User result = new User();
        result.setAccessLevel(AccessLevel.ADMIN);
        result.setDiscordId(discordId);
        return result;
    }

    @Test
    public void shouldCallTestCommandIfCallTriggered() {
        final CommandCall call = CommandCall.builder()
                .author(aStandardUser())
                .commandPhrase("test")
                .build();
        assertThat(callParameters, is(empty()));
        commandRegistry.fireEvent(call);
        assertThat(callParameters, is(not(empty())));
        assertThat(callParameters.get(0), is(call));
    }

    @Test
    public void adminShouldBeAllowedToCallAdminMethod() {
        final CommandCall call = CommandCall.builder()
                .author(anAdminUser())
                .commandPhrase("admin")
                .build();
        commandRegistry.fireEvent(call);
        assertThat(callParameters, is(not(empty())));
    }

    @Test
    public void normalUserShouldNotBeAllowedToCallAdminMethod() {
        final CommandCall call = CommandCall.builder()
                .author(aStandardUser())
                .commandPhrase("admin")
                .build();
        commandRegistry.fireEvent(call);
        assertThat(callParameters, is(empty()));
    }

    @Test
    public void shouldCallTestCommandForMultiNameMethod() {
        final CommandCall call1 = CommandCall.builder()
                .commandPhrase("test1")
                .author(aStandardUser())
                .build();
        commandRegistry.fireEvent(call1);
        assertThat(callParameters.get(0), is(call1));

        callParameters.remove(0);

        final CommandCall call2 = CommandCall.builder()
                .commandPhrase("test2")
                .author(aStandardUser())
                .build();
        commandRegistry.fireEvent(call2);
        assertThat(callParameters.get(0), is(call2));
    }

    @DiscordController
    @Configuration
    public static class TestClass {
        @DiscordCommand(name = "test")
        public void testCommand1(final CommandCall call) {
            callParameters.add(call);
        }

        @DiscordCommand(name = {"test1", "test2"})
        public void testCommand2(final CommandCall call) {
            callParameters.add(call);
        }

        @DiscordCommand(name = "admin", roleRequired = AccessLevel.ADMIN)
        public void adminCommand(final CommandCall call) {
            callParameters.add(call);
        }
    }
}
