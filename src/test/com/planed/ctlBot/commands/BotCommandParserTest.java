package com.planed.ctlBot.commands;

import org.junit.Test;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public class BotCommandParserTest {
    @Test
    public void receiveHelloCommand_ShouldReturnHelloCommand(){
        BotCommand command = BotCommandParser.parse(createMessageMock("!helloWorld"));
    }

    private MessageReceivedEvent createMessageMock(String s) {
        MessageReceivedEvent result = Mockito.mock(MessageReceivedEvent.class);
        when(result.)
        return result;
    }
}
