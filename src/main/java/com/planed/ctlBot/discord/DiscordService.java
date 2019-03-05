package com.planed.ctlBot.discord;

import com.planed.ctlBot.commands.data.DiscordMessage;
import com.planed.ctlBot.utils.DiscordMessageParser;
import com.planed.ctlBot.utils.StringConstants;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Component
public class DiscordService {
    private static final Logger logger = LoggerFactory.getLogger(DiscordService.class);
    private static final String INFO_EMOJI = "ℹ";

    @Autowired
    private DiscordApi discordApi;
    @Autowired
    private DiscordMessageParser discordMessageParser;
    @Value("${dasLigaBot.dasLigaServer.id}")
    private String dasLigaServerId;

    public boolean isUserYourself(String userId) {
        return discordApi.getUserById(userId).join().isYourself();
    }

    public Optional<DiscordMessage> replyInChannel(final Long serverId, final Long channelId, final String message) {
        logger.debug(channelId + ": " + message);
        return discordApi.getServerById(serverId)
                .flatMap(server -> server.getChannelById(channelId))
                .flatMap(channel -> channel.asTextChannel())
                .map(channel -> channel.sendMessage(message))
                .flatMap(future -> joinAndParseMessageSafe(future,
                        () -> "Error while sending message to server " + serverId + " and channel " + channelId + "!"))
                .map(msg -> addInfoItemAndListenerToMessage(msg));
    }

    public Optional<DiscordMessage> replyInChannel(TextChannel channel, final String message) {
        logger.debug(channel + ": " + message);
        return joinAndParseMessageSafe(channel.sendMessage(message),
                () -> "Error while sending message to channel " + channel + "!")
                .map(msg -> addInfoItemAndListenerToMessage(msg));
    }

    public Optional<DiscordMessage> whisperToUser(final String userId, final String message) {
        return discordApi.getUserById(userId).join()
                .openPrivateChannel().join()
                .getCurrentCachedInstance()
                .map(channel -> channel.sendMessage(message))
                .flatMap(future -> joinAndParseMessageSafe(future,
                        () -> "Error while sending whisper message to user " + userId + "!"))
                .map(msg -> addInfoItemAndListenerToMessage(msg));
    }

    public Optional<DiscordMessage> whisperToUser(final User user, final String message) {
        return user
                .openPrivateChannel().join()
                .getCurrentCachedInstance()
                .map(channel -> channel.sendMessage(message))
                .flatMap(future -> joinAndParseMessageSafe(future,
                        () -> "Error while sending whisper message to user " + user.getName() + "!"))
                .map(msg -> addInfoItemAndListenerToMessage(msg));
    }

    public DiscordMessage addReactionWithMapper(final DiscordMessage message,
                                                final List<String> reactions, Consumer<String> reactionAddConsumer) {
        message.getMessage().addReactionAddListener(event -> {
            if (event.getUser().isYourself()) {
                return;
            }
            final String emojiAsUnicode = event.getEmoji().asUnicodeEmoji().get();
            reactionAddConsumer.accept(emojiAsUnicode);
        });

        reactions.stream().forEach(reaction -> message.getMessage().addReaction(reaction).join());

        return message;
    }

    public String getDiscordName(final String discordId, Long serverId) {
        return Optional.ofNullable(serverId)
                .flatMap(sId -> discordApi.getServerById(sId))
                .flatMap(server -> discordApi.getUserById(discordId).join().getNickname(server))
                .orElse(discordApi.getUserById(discordId).join().getName());
    }

    public void addRoleToUser(final String discordId, final String roleName) {
        final Server server = discordApi.getServerById(dasLigaServerId)
                .orElseThrow(() -> new RuntimeException("Couldn't find DasLigaServer by id '" + dasLigaServerId + "'!"));

        final Role role = server
                .getRolesByNameIgnoreCase(roleName).stream().findAny()
                .orElseThrow(() -> new RuntimeException("Couldn't find role by name '" + roleName + "'!"));

        discordApi.getUserById(discordId)
                .thenApply(user -> server.addRoleToUser(user, role))
                .join();
    }

    public void removeRolesFromUser(String discordId, List<String> roles) {
        final Server server = discordApi.getServerById(dasLigaServerId)
                .orElseThrow(() -> new RuntimeException("Couldn't find DasLigaServer by id '" + dasLigaServerId + "'!"));

        roles.stream()
                .map(roleName -> server
                        .getRolesByNameIgnoreCase(roleName.replace("_", " ")).stream().findAny()
                        .orElseThrow(() -> new RuntimeException("Couldn't find role by name '" + roleName + "'!")))
                .forEach(role -> discordApi.getUserById(discordId)
                        .thenApply(user -> server.removeRoleFromUser(user, role))
                        .join());
    }

    private DiscordMessage addInfoItemAndListenerToMessage(DiscordMessage msg) {
        addReactionWithMapper(msg, Collections.singletonList(INFO_EMOJI),
                s -> {
                    if (s.equals(INFO_EMOJI)) {
                        final User otherUser = ((PrivateChannel) msg.getTextChannel()).getRecipient();
                        whisperToUser(otherUser, StringConstants.INFO_STRING);
                    }
                });
        return msg;
    }

    public String getInviteLink() {
        return discordApi.createBotInvite();
    }

    public Optional<Server> findServerById(Long serverId) {
        return discordApi.getServerById(serverId);
    }

    private Optional<DiscordMessage> joinAndParseMessageSafe(CompletableFuture<Message> future,
                                                             Supplier<String> errorMessageSupplier) {
        return future
                .thenApply(msg -> Optional.of(discordMessageParser.deconstructMessage(msg))).exceptionally(t -> {
                    logger.warn(errorMessageSupplier.get(), t);
                    return Optional.empty();
                })
                .join();
    }

    public String getDiscordName(String discordId) {
        User user = discordApi.getUserById(discordId).join();
        return user.getName();
    }
}
