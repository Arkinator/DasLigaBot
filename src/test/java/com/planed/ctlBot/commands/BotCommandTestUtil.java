package com.planed.ctlBot.commands;

import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Message;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Julian Peters on 10.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public class BotCommandTestUtil {
    public static MessageReceivedEvent createMessageMock(String messageString, String authorId) {
        MessageReceivedEvent result = mock(MessageReceivedEvent.class);
        Message message = mock(Message.class);
        when(result.getMessage()).thenReturn(message);
        when(message.getContent()).thenReturn(messageString);
        when(message.getChannel()).thenReturn(mock(IChannel.class));
        IUser authorMock = mock(IUser.class);
        when(authorMock.getID()).thenReturn(authorId);
        when(message.getAuthor()).thenReturn(authorMock);
        return result;
    }

    public static MessageReceivedEvent createMessageMock(String messageString) {
        return createMessageMock(messageString, "");
    }

    public static String buildMentionTag(String author) {
        return "<@"+author+">";
    }
}
