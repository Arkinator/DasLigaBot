package com.planed.ctlBot.commands;

import org.junit.Before;
import org.junit.Test;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import static com.planed.ctlBot.commands.BotCommandTestUtil.createMessageMock;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public class BotCommandParserTest {
    private BotCommand commandMock;
    private BotCommandParser parser;
    private final String COMMAND_PHRASE = "jfkdsl";

    @Before
    public void setUp() {
        commandMock = mock(BotCommand.class);
        parser = new BotCommandParser();
        parser.register(COMMAND_PHRASE, commandMock);
    }

    @Test
    public void receiveHelloCommand_ShouldReturnHelloCommand(){
        MessageReceivedEvent message = createMessageMock("!"+COMMAND_PHRASE);
        parser.parseAndExecute(message);
        verify(commandMock,times(1)).execute(message);
    }

    @Test
    public void receiveHelloCommandInUppercase_ShouldReturnHelloCommand(){
        MessageReceivedEvent message = createMessageMock("!"+COMMAND_PHRASE.toUpperCase());
        parser.parseAndExecute(message);
        verify(commandMock,times(1)).execute(message);
    }

    @Test
    public void receiveCommandWithoutExclamationMark_ShouldNotExecuteCommand(){
        MessageReceivedEvent message = createMessageMock(COMMAND_PHRASE);
        parser.parseAndExecute(message);
        verify(commandMock,times(0)).execute(any());
    }

    @Test
    public void receiveUnknownCommand_ShouldReturnNoCommand(){
        MessageReceivedEvent message = createMessageMock("!blöder Text");
        parser.parseAndExecute(message);
        verify(commandMock,times(0)).execute(any());
    }
}

