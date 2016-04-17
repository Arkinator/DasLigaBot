package com.planed.ctlBot.services;

import com.planed.ctlBot.commands.BotCommand;
import com.planed.ctlBot.commands.data.CommandCall;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.Matchers.any;
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
public class CommandServiceTest {
    @Mock
    BotCommand commandMock;

    CommandService commandService;
    String commandName;
    private MessageReceivedEvent mockMessage;
    private IMessage mockDiscordMessage;
    private IUser mockAuthor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        commandService = new CommandService();
        commandName = RandomStringUtils.randomAlphanumeric(10);
        commandService.subscribe(commandMock, commandName);

        mockMessage = mock(MessageReceivedEvent.class);
        mockDiscordMessage = mock(IMessage.class);
        mockAuthor = mock(IUser.class);
        when(mockMessage.getMessage()).thenReturn(mockDiscordMessage);
        when(mockDiscordMessage.getContent()).thenReturn("!" + commandName);
        when(mockDiscordMessage.getAuthor()).thenReturn(mockAuthor);
    }

    @Test
    public void expectCallWhenCommandMatches() {
        commandService.parseMessage(mockMessage);
        verify(commandMock, times(1)).messageReceiver(any());
    }

    @Test
    public void expectCallWhenCalledWithParameters() {
        when(mockDiscordMessage.getContent()).thenReturn("!" + commandName + " param1 param2");
        commandService.parseMessage(mockMessage);
        verify(commandMock, times(1)).messageReceiver(any());
    }

    @Test
    public void expectNoCallWhenCommandDoesNotMatch() {
        when(mockDiscordMessage.getContent()).thenReturn(RandomStringUtils.randomAlphanumeric(20));
        commandService.parseMessage(mockMessage);
        verify(commandMock, times(0)).messageReceiver(any());
    }

    @Test
    public void expectNoCallWhenMessageEmpty() {
        when(mockDiscordMessage.getContent()).thenReturn("");
        commandService.parseMessage(mockMessage);
        verify(commandMock, times(0)).messageReceiver(any());
    }

    @Test
    public void expectCallWithCorrectAuthor() {
        String messageAuthor = RandomStringUtils.randomAlphanumeric(20);
        when(mockAuthor.getID()).thenReturn(messageAuthor);
        commandService.parseMessage(mockMessage);

        ArgumentCaptor<CommandCall>captor= ArgumentCaptor.forClass(CommandCall.class);
        verify(commandMock).messageReceiver(captor.capture());
        assertThat(captor.getValue().getAuthorId(), equalTo(messageAuthor));
    }

    @Test
    public void expectCallWithCorrectParameters() {
        when(mockDiscordMessage.getContent()).thenReturn("!" + commandName + " param1 param2");
        commandService.parseMessage(mockMessage);

        ArgumentCaptor<CommandCall>captor= ArgumentCaptor.forClass(CommandCall.class);
        verify(commandMock).messageReceiver(captor.capture());
        assertThat(captor.getValue().getParameters(), contains("param1", "param2"));
    }

    @Test
    public void expectCallWithMentions() {
        List<IUser> mentionsList = new ArrayList<>();
        IUser mockMentionedUser = mock(IUser.class);
        mentionsList.add(mockMentionedUser);
        String anotherMockedUserId = RandomStringUtils.randomAlphanumeric(20);
        when(mockMentionedUser.getID()).thenReturn(anotherMockedUserId);
        when(mockDiscordMessage.getMentions()).thenReturn(mentionsList);
        commandService.parseMessage(mockMessage);

        ArgumentCaptor<CommandCall>captor= ArgumentCaptor.forClass(CommandCall.class);
        verify(commandMock).messageReceiver(captor.capture());
        assertThat(captor.getValue().getMentions(), contains(anotherMockedUserId));
    }
}
