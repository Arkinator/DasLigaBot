package com.planed.ctlBot.commands;

import com.planed.ctlBot.CtlDataStore;
import com.planed.ctlBot.InterfaceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import java.util.Optional;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
public class BotCommandParser {
    private Logger LOG = LoggerFactory.getLogger(InterfaceListener.class);

    private final String COMMAND_START_SYMBOL = "!";
    private CtlDataStore ctlDataStore;

    @Autowired
    BotCommandParser(CtlDataStore ctlDataStore){
        this.ctlDataStore = ctlDataStore;
    }
    public BotCommand parse(MessageReceivedEvent event) {
        Optional<String> commandText = getCommandText(event.getMessage().getContent());
        if (commandText.isPresent()){
            switch (commandText.get()) {
                case HelloWorldCommand.COMMAND_STRING:
                    return new HelloWorldCommand(event);
                case AddMatchCommand.COMMAND_STRING:
                    return new AddMatchCommand(ctlDataStore, event);
                default:
                    LOG.info("Command not recognized: '"+commandText.get()+"'");
                    return new NoCommand(event);
            }
        }else{
            LOG.info("no command found in message '"+event.getMessage().getContent());
            return new NoCommand(event);
        }
    }

    private Optional<String> getCommandText(final String content) {
        String trimmedMessage = content.trim();
        if (!trimmedMessage.substring(0,1).equals(COMMAND_START_SYMBOL)){
            return Optional.empty();
        } else {
            String[] parts = trimmedMessage.substring(1).split(" ");
            return Optional.of(parts[0]);
        }
    }
}
