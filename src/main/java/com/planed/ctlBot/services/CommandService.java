package com.planed.ctlBot.services;

import com.planed.ctlBot.commands.BotCommand;
import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.utils.DiscordMessageParser;
import org.springframework.stereotype.Service;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Julian Peters on 17.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Service
public class CommandService {
    private Map<String, BotCommand> commandMap = new HashMap<>();

    public void parseMessage(MessageReceivedEvent message) {
        Optional<CommandCall> commandCallOptional = DiscordMessageParser.deconstructMessage(message);
        if (commandCallOptional.isPresent()) {
            processBotCommand(commandCallOptional.get());
        }
    }

    private void processBotCommand(CommandCall commandCall) {
        BotCommand command = commandMap.get(commandCall.getCommandPhrase());
        if (command != null) {
            command.messageReceiver(commandCall);
        }
    }

    public void subscribe(BotCommand command, String commandName) {
        commandMap.put(commandName, command);
    }
}
