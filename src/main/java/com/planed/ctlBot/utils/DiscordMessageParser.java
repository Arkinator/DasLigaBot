package com.planed.ctlBot.utils;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.services.CommandCallImpl;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by Julian Peters on 17.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public final class DiscordMessageParser {
    private DiscordMessageParser(){

    }

    public static Optional<CommandCall> deconstructMessage(MessageReceivedEvent message) {
        String messageContent = message.getMessage().getContent();
        if (messageContent == null || messageContent.length() == 0) {
            return Optional.empty();
        }
        CommandCallImpl result = new CommandCallImpl();
        List<String> commandParts = new ArrayList<String>(Arrays.asList(messageContent.substring(1).split(" ")));
        result.setCommandPhrase(commandParts.remove(0));
        result.setAuthorId(message.getMessage().getAuthor().getID());
        commandParts.forEach(s -> result.addParameter(s));
        message.getMessage().getMentions().forEach(user->result.addMention(user.getID()));
        return Optional.of(result);
    }
}
