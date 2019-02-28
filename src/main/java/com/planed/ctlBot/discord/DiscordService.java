package com.planed.ctlBot.discord;

import com.planed.ctlBot.commands.data.DiscordMessage;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.utils.DiscordMessageParser;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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

    public DiscordMessage replyInChannel(final String serverId, final String channelId, final String message) {
        logger.info(channelId + ": " + message);
        return discordApi.getServerById(serverId)
                .flatMap(server -> server.getChannelById(channelId))
                .flatMap(channel -> channel.asTextChannel())
                .map(channel -> channel.sendMessage(message).join())
                .flatMap(msg -> discordMessageParser.deconstructMessage(msg))
                .orElseThrow(() -> new RuntimeException("Error while sending message to server " + serverId + " and channel " + channelId + "!"));
    }

    public DiscordMessage whisperToUser(final String userId, final String message) {
        return discordApi.getUserById(userId).join()
                .openPrivateChannel().join()
                .getCurrentCachedInstance()
                .map(channel -> channel.sendMessage(message).join())
                .flatMap(msg -> discordMessageParser.deconstructMessage(msg))
                .orElseThrow(() -> new RuntimeException("Error while sending whisper message to user " + userId + "!"));
    }

    public DiscordMessage addReactionWithMapper(final DiscordMessage message, List<String> reactions, Consumer<String> reactionAddConsumer) {
        TextChannel textChannel = discordApi
                .getChannelById(message.getChannel())
                .flatMap(channel -> channel.asTextChannel())
                .orElseThrow(() -> new RuntimeException("Unable to get " + message.getChannel() + " as a text channel!"));
        final Message internalMessage = discordApi.getMessageById(message.getMessageId(), textChannel).join();
        reactions.stream()
                .forEach(reaction -> internalMessage.addReaction(reaction).join());
        internalMessage.addReactionAddListener(event -> {
            if (event.getUser().isYourself()) {
                return;
            }
            final String emojiAsUnicode = event.getEmoji().asUnicodeEmoji().get();
            reactionAddConsumer.accept(emojiAsUnicode);
        });
        return message;
    }

    public String shortInfo(final User user, String serverId) {
        String result = getDiscordName(user, serverId);
        result += " (" + user.getElo() + ")";
        return result;
    }

    public String shortInfo(final User user) {
        return shortInfo(user, null);
    }

    public String getDiscordName(final User user, String serverId) {
        return Optional.ofNullable(serverId)
                .flatMap(sId -> discordApi.getServerById(sId))
                .flatMap(server -> discordApi.getUserById(user.getDiscordId()).join().getNickname(server))
                .orElse(discordApi.getUserById(user.getDiscordId()).join().getName());
    }

    public String getInviteLink() {
        return discordApi.createBotInvite();
    }
}
