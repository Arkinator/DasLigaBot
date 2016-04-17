package com.planed.ctlBot.commands;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.services.CommandService;
import com.planed.ctlBot.services.DiscordService;
import com.planed.ctlBot.services.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Julian Peters on 17.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public class InfoCommandTest {
    @Mock
    UserService userService;

    @Mock
    DiscordService discordService;

    @Mock
    CommandService commandService;

    @InjectMocks
    InfoCommand infoCommand;

    @Before
    public void setup(){
        initMocks(this);
    }

    @Test
    public void verifyWhisperToPerson() throws Exception {
        String authorId = RandomStringUtils.randomAlphanumeric(20);
        CommandCall commandMock = mock(CommandCall.class);
        when(commandMock.getAuthorId()).thenReturn(authorId);

        infoCommand.messageReceiver(commandMock);
        verify(discordService, times(1)).whisperToPerson(eq(authorId), anyString());
    }
}
