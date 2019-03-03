package com.planed.ctlBot.utils;

import com.planed.ctlBot.commands.data.DiscordMessage;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.services.ServerService;
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
    @Autowired
    private UserService userService;
    @Autowired
    private ServerService serverService;

    public DiscordMessage deconstructMessage(final Message message) {
        final String messageContent = message.getContent();
        final org.javacord.api.entity.user.User discordUser = message.getAuthor().asUser().get();
        final DiscordMessage.DiscordMessageBuilder messageBuilder = DiscordMessage.builder()
                .author(userService.findUserAndCreateIfNotFound(message.getAuthor().getIdAsString()))
                .discordUser(discordUser)
                .channel(message.getChannel().getId())
                .messageId(Long.toString(message.getId()))
                .serverId(message.getServer()
                        .map(server -> server.getId())
                        .orElse(null))
                .textChannel(message.getChannel())
                .message(message)
                .mentionedChannels(message.getMentionedChannels())
                .mentions(extractMentionsFromMessage(message));

        Optional<String> commandPrefix = getCommandPrefixesForUser(discordUser).stream()
                .filter(prefix -> messageContent.startsWith(prefix))
                .findFirst();

        if (!commandPrefix.isPresent()) {
            return messageBuilder.build();
        } else {
            final List<String> commandParts = new ArrayList<>(Arrays.asList(messageContent.substring(commandPrefix.get().length()).split(" ")));
            return messageBuilder
                    .commandPhrase(commandParts.remove(0))
                    .parameters(commandParts)
                    .build();
        }
    }

    private List<String> getCommandPrefixesForUser(org.javacord.api.entity.user.User discordUser) {
        final List<String> list = discordUser.getMutualServers().stream()
                .map(server -> serverService.getServerPrefix(server.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .collect(Collectors.toList());
        list.add("!");
        return list;
    }

    private List<User> extractMentionsFromMessage(Message message) {
        return message.getMentionedUsers().stream()
                .map(user -> userService.findUserAndCreateIfNotFound(user.getIdAsString()))
                .collect(Collectors.toList());
    }
}
