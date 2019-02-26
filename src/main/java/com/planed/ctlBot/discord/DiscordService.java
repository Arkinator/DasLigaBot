package com.planed.ctlBot.discord;

import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.services.UserService;
import org.javacord.api.DiscordApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiscordService {
    Logger LOG = LoggerFactory.getLogger(DiscordService.class);

    private String commandList;
    @Autowired
    private DiscordApi discordApi;
    @Autowired
    private UserService userService;

    public void replyInChannel(final String serverId, final String channelId, final String message) {
        LOG.info(channelId + ": " + message);
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

    public String shortInfo(final User user) {
        String result = getDiscordName(user);
        result += " (" + user.getElo() + ")";
        return result;
    }

    public String getDiscordName(final User user) {
        return discordApi.getUserById(user.getDiscordId()).join().getName();
    }
}
