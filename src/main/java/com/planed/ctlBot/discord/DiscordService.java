package com.planed.ctlBot.discord;

import com.planed.ctlBot.domain.User;
import org.javacord.api.DiscordApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DiscordService {
    private static final Logger logger = LoggerFactory.getLogger(DiscordService.class);

    @Autowired
    private DiscordApi discordApi;

    public void replyInChannel(final String serverId, final String channelId, final String message) {
        logger.info(channelId + ": " + message);
        discordApi.getServerById(serverId)
                .flatMap(server -> server.getChannelById(channelId))
                .flatMap(channel -> channel.asTextChannel())
                .ifPresent(channel -> channel.sendMessage(message));
    }

    public void whisperToUser(final String userId, final String message) {
        discordApi.getUserById(userId).join()
                .openPrivateChannel().join()
                .getCurrentCachedInstance()
                .ifPresent(channel -> channel.sendMessage(message));
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
