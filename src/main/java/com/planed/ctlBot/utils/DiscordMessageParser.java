package com.planed.ctlBot.utils;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.services.UserService;
import org.javacord.api.entity.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public Optional<CommandCall> deconstructMessage(final Message message) {
        final String messageContent = message.getContent();
        if (messageContent == null || messageContent.length() == 0 || !messageContent.startsWith("!")) {
            return Optional.empty();
        }
        final List<String> commandParts = new ArrayList<>(Arrays.asList(messageContent.substring(1).split(" ")));
        final List<User> mentions = new ArrayList<>();
        message.getMentionedUsers().forEach(user -> mentions.add(
                userService.findUserAndCreateIfNotFound(user.getIdAsString())));
        final CommandCall result = CommandCall.builder()
                .author(userService.findUserAndCreateIfNotFound(message.getAuthor().getIdAsString()))
                .channel(message.getChannel().getIdAsString())
                .commandPhrase(commandParts.remove(0))
                .parameters(commandParts)
                .serverId(message.getServer()
                        .map(server -> server.getIdAsString())
                        .orElse(null))
                .mentions(mentions)
                .build();
        return Optional.of(result);
    }
}
