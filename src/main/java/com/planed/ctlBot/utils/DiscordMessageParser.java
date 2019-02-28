package com.planed.ctlBot.utils;

import com.planed.ctlBot.commands.data.DiscordMessage;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.services.UserService;
import org.javacord.api.entity.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DiscordMessageParser {
    private final UserService userService;

    @Autowired
    public DiscordMessageParser(final UserService userService) {
        this.userService = userService;
    }

    public Optional<DiscordMessage> deconstructMessage(final Message message) {
        final String messageContent = message.getContent();
        final DiscordMessage.DiscordMessageBuilder messageBuilder = DiscordMessage.builder()
                .author(userService.findUserAndCreateIfNotFound(message.getAuthor().getIdAsString()))
                .channel(message.getChannel().getIdAsString())
                .messageId(Long.toString(message.getId()))
                .serverId(message.getServer()
                        .map(server -> server.getIdAsString())
                        .orElse(null))
                .mentions(extractMentionsFromMessage(message));

        if (messageContent == null || messageContent.length() == 0 || !messageContent.startsWith("!")) {
            return Optional.of(messageBuilder.build());
        } else {
            final List<String> commandParts = new ArrayList<>(Arrays.asList(messageContent.substring(1).split(" ")));
            return Optional.of(messageBuilder
                    .commandPhrase(commandParts.remove(0))
                    .parameters(commandParts)
                    .build());
        }
    }

    private List<User> extractMentionsFromMessage(Message message) {
        return message.getMentionedUsers().stream()
                .map(user -> userService.findUserAndCreateIfNotFound(user.getIdAsString()))
                .collect(Collectors.toList());
    }
}
