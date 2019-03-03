package com.planed.ctlBot.discord;

import com.planed.ctlBot.commands.data.DiscordMessage;
import com.planed.ctlBot.utils.DiscordMessageParser;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Component
public class DiscordService {
    private static final Logger logger = LoggerFactory.getLogger(DiscordService.class);

    @Autowired
    private DiscordApi discordApi;
    @Autowired
    private DiscordMessageParser discordMessageParser;

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
                        () -> "Error while sending message to server " + serverId + " and channel " + channelId + "!"));
    }

    public Optional<DiscordMessage> replyInChannel(TextChannel channel, final String message) {
        logger.debug(channel + ": " + message);
        return joinAndParseMessageSafe(channel.sendMessage(message),
                () -> "Error while sending message to channel " + channel + "!");
    }

    public Optional<DiscordMessage> whisperToUser(final String userId, final String message) {
        return discordApi.getUserById(userId).join()
                .openPrivateChannel().join()
                .getCurrentCachedInstance()
                .map(channel -> channel.sendMessage(message))
                .flatMap(future -> joinAndParseMessageSafe(future,
                        () -> "Error while sending whisper message to user " + userId + "!"));
    }

    public Optional<DiscordMessage> whisperToUser(final User user, final String message) {
        return user
                .openPrivateChannel().join()
                .getCurrentCachedInstance()
                .map(channel -> channel.sendMessage(message))
                .flatMap(future -> joinAndParseMessageSafe(future,
                        () -> "Error while sending whisper message to user " + user.getName() + "!"));
    }

    public DiscordMessage addReactionWithMapper(final DiscordMessage message,
                                                List<String> reactions, Consumer<String> reactionAddConsumer) {
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
}
