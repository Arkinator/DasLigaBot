package com.planed.ctlBot.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
public class BotCommandParser {
    private Logger LOG = LoggerFactory.getLogger(BotCommandParser.class);

    private final String COMMAND_START_SYMBOL = "!";
    private HashMap<String, BotCommand> commandMap;

    BotCommandParser(){
        commandMap = new HashMap<>();
    }

    public void register(String commandName, BotCommand command){
        if (commandMap.get(commandName) == null){
            commandMap.put(commandName.toLowerCase(), command);
        }
    }

    public void parseAndExecute(MessageReceivedEvent event) {
        Optional<String> commandText = getCommandText(event.getMessage().getContent());
        if (commandText.isPresent()){
            BotCommand command = commandMap.get(commandText.get().toLowerCase());
            if (command != null) {
                if (command.doesUserHaveNecessaryLevel(event)) {
                    command.execute(event);
                }else{
                    LOG.info("No command authorization: '"+commandText.get()+"' for user "+event.getMessage().getAuthor().getName());
                }
            }else{
                LOG.info("Command not recognized: '"+commandText.get()+"'");
            }
        }else{
            LOG.info("no command found in message '"+event.getMessage().getContent());
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

    public Set<Map.Entry<String, BotCommand>> getAllRegisteredCommands() {
        return commandMap.entrySet();
    }
}
