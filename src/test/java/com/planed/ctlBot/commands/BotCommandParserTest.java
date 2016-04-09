package com.planed.ctlBot.commands;

import com.planed.ctlBot.CtlDataStore;
import org.junit.Before;
import org.junit.Test;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Message;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public class BotCommandParserTest {
    private IDiscordClient clientMock;
    private IUser userMock1;
    private IUser userMock2;
    private IChannel channelMock;
    private BotCommandParser parser;

    @Before
    public void setUp() {
        clientMock = mock(IDiscordClient.class);
        userMock1 = mock(IUser.class);
        userMock2 = mock(IUser.class);
        channelMock = mock(IChannel.class);
        parser = new BotCommandParser(mock(CtlDataStore.class));
    }

    @Test
    public void receiveHelloCommand_ShouldReturnHelloCommand(){
        BotCommand command = parser.parse(createMessageMock("!hello"));
        assertThat(command, instanceOf(HelloWorldCommand.class));
    }

    @Test
    public void receiveNoCommand_ShouldReturnNoCommand(){
        BotCommand command = parser.parse(createMessageMock("blöder Text"));
        assertThat(command, instanceOf(NoCommand.class));
    }

    @Test
    public void receiveUnknownCommand_ShouldReturnNoCommand(){
        BotCommand command = parser.parse(createMessageMock("!blöder Text"));
        assertThat(command, instanceOf(NoCommand.class));
    }

    @Test
    public void receiveHelloCommand_ShouldWhisperBack() throws Exception {
        BotCommand command = parser.parse(createMessageMock("!hello"));
        command.execute();
        verify(channelMock,times(1)).sendMessage("Hello World!");
    }

    @Test
    public void receiveAddMatch_shouldReturnAddMatchCommand() throws Exception {
        BotCommand command = parser.parse(createMessageMock("!addMatch @FusTup @CtlBattleReporter", userMock1, userMock2));
        assertThat(command, instanceOf(AddMatchCommand.class));
    }

    private MessageReceivedEvent createMessageMock(String messageString, IUser... mentions) {
        MessageReceivedEvent result = mock(MessageReceivedEvent.class);
        Message message = mock(Message.class);
        when(result.getMessage()).thenReturn(message);
        when(message.getContent()).thenReturn(messageString);
        when(result.getClient()).thenReturn(clientMock);
        when(message.getAuthor()).thenReturn(userMock1);
        when(message.getChannel()).thenReturn(channelMock);
        when(message.getMentions()).thenReturn(Arrays.asList(mentions));
        return result;
    }
}

