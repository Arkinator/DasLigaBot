package com.planed.ctlBot;

import com.planed.ctlBot.commands.BotCommand;
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
        BotCommand command = parser.parse(message);
        LOG.info("Message received: '"+message.getMessage().getContent()+"'. Resulting in command "+command.getClass().getCanonicalName());
        if (command.isValidCommand()){
            command.execute();
        }
    }
}
