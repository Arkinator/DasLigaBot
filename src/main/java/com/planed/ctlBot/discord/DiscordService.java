package com.planed.ctlBot.discord;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.data.UserEntity;
import com.planed.ctlBot.data.repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MissingPermissionsException;


/**
 * Created by Julian Peters on 17.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
public class DiscordService {
    private IDiscordClient discordClient;
    private final UserEntityRepository userEntityRepository;

    @Autowired
    public DiscordService(final UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    public void replyInChannel(final String channelId, final String message) {
        try {
            discordClient.getChannelByID(channelId).sendMessage(message);
        } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
            e.printStackTrace();
        }
    }

    public UserEntity createNewUserFromId(final String discordId) {
        final UserEntity entity = fillUserEntityObject(discordId);
        userEntityRepository.save(entity);
        return userEntityRepository.findByDiscordId(discordId);
    }

    private UserEntity fillUserEntityObject(final String discordId) {
        Assert.notNull(discordClient);
        discordClient.getUserByID(discordId);
        final UserEntity result = new UserEntity();
        result.setDiscordId(discordId);
        result.setAccessLevel(AccessLevel.User);
        result.setNumberOfInteractions(0);
        return result;
    }

    public void setDiscordClient(final IDiscordClient discordClient) {
        this.discordClient = discordClient;
    }
}
