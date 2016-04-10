package com.planed.ctlBot.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
public class HelloCommand extends AbstractBotCommand {
    private Logger LOG = LoggerFactory.getLogger(HelloCommand.class);
    public static final String COMMAND_STRING = "hello";

    @Autowired
    HelloCommand(BotCommandParser parser) {
        parser.register(COMMAND_STRING, this);
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        String name = event.getMessage().getAuthor().getName();
        replyInChannel(event, "Hello "+name+", how U doin?");
        LOG.info("Saying hello to "+name+", with id="+getAuthor(event));
    }

    @Override
    public String getHelpText() {
        return "Say hello to me! YAY!";
    }
}
