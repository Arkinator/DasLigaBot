package com.planed.ctlBot.utils;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.commands.data.CommandCallBuilder;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class DiscordMessageParser {
    private final UserService userService;

    @Autowired
    public DiscordMessageParser(final UserService userService) {
        this.userService = userService;
    }

    public Optional<CommandCall> deconstructMessage(final MessageReceivedEvent message) {
        final String messageContent = message.getMessage().getContent();
        if (messageContent == null || messageContent.length() == 0 || !messageContent.startsWith("!")) {
            return Optional.empty();
        }
        final List<String> commandParts = new ArrayList<>(Arrays.asList(messageContent.substring(1).split(" ")));
        final List<User> mentions = new ArrayList<>();
        message.getMessage().getMentions().forEach(user->mentions.add(
                userService.findUserAndCreateIfNotFound(user.getID())));
        final CommandCall result = new CommandCallBuilder()
                .setAuthor(userService.findUserAndCreateIfNotFound(message.getMessage().getAuthor().getID()))
                .setChannel(message.getMessage().getChannel().getID())
                .setCommandPhrase(commandParts.remove(0))
                .setParameterList(commandParts)
                .setMentionsList(mentions)
                .createCommandCall();
        return Optional.of(result);
    }
}
