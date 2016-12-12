package com.planed.ctlBot.commands;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.commands.data.CommandCallBuilder;
import com.planed.ctlBot.discord.CommandRegistry;
import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.domain.UserRepository;
import com.planed.ctlBot.teamup.TeamupService;
import com.planed.ctlBot.teamup.data.TeamupEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;

import static com.planed.ctlBot.domain.UserFixtures.aDefaultUser;
import static com.planed.ctlBot.domain.UserFixtures.anAdmin;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("development")
public class TeamupCommandsTest {
    @Autowired
    private CommandRegistry commandRegistry;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private TeamupService teamupService;
    @MockBean
    private DiscordService discordService;

    private User user;
    private User admin;

    @Before
    public void setUp() {
        user = newUser(aDefaultUser());
        admin = newUser(anAdmin());

        doReturn(Collections.singletonList(aStandardEvent())).when(teamupService).getEventsBetweenDates(any(), any());
    }

    @Test
    public void shouldGiveEventsForNextWeek() {
        commandRegistry.fireEvent(aSimpleCommand(user, "eventsNextWeek"));

        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        verify(discordService).replyInChannel(eq(aSimpleCommand(user,"").getChannel()), stringCaptor.capture());
        assertThat(stringCaptor.getValue()).contains(aStandardEvent().getTitle());
    }

    private CommandCall aSimpleCommand(final User user, final String command, final String... parameters) {
        return new CommandCallBuilder()
                .setAuthor(user)
                .setCommandPhrase(command)
                .setChannel("fjkdsl")
                .setParameterList(Arrays.asList(parameters))
                .createCommandCall();
    }

    private TeamupEvent aStandardEvent() {
        TeamupEvent event = new TeamupEvent();
        event.setTitle("event title");
        event.setWho("who?");
        event.setStart(OffsetDateTime.now());
        event.setEnd(OffsetDateTime.now().plusHours(1));
        return event;
    }

    private User newUser(final User user) {
        userRepository.save(user);
        return userRepository.findByDiscordId(user.getDiscordId());
    }
}
