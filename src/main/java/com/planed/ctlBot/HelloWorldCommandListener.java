package com.planed.ctlBot;

import com.planed.ctlBot.commands.BotCommandParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public class HelloWorldCommandListener {
    private final BotCommandParser parser;
    Logger LOG = LoggerFactory.getLogger(InterfaceListener.class);

    public HelloWorldCommandListener(BotCommandParser parser) {
        this.parser = parser;
    }

    @EventSubscriber
    public void helloWorldMessage(MessageReceivedEvent message) {
        LOG.debug("Message received: '"+message.getMessage().getContent());
        parser.parseAndExecute(message);
    }
}
