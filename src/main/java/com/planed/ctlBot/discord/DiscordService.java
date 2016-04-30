package com.planed.ctlBot.discord;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.data.UserEntity;
import com.planed.ctlBot.data.repositories.UserEntityRepository;
import com.planed.ctlBot.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;


/**
 * Created by Julian Peters on 17.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
public class DiscordService {
    Logger LOG = LoggerFactory.getLogger(DiscordService.class);

    private IDiscordClient discordClient;
    private final UserEntityRepository userEntityRepository;
    private String commandList;

    @Autowired
    public DiscordService(final UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    public void replyInChannel(final String channelId, final String message) {
        RequestBuffer.request(() -> {
            try {
                LOG.info("building message " + message);
                new MessageBuilder(discordClient)
                        .withChannel(channelId)
                        .withContent(message).build();
                LOG.info("\tbuild message " + message);
            } catch (MissingPermissionsException | DiscordException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public void whisperToUser(final String authorId, final String message) {
        try {
            LOG.warn(message);
            final IChannel privateChannel = discordClient.getOrCreatePMChannel(discordClient.getUserByID(authorId));
            replyInChannel(privateChannel.getID(), message);
        } catch (HTTP429Exception | DiscordException e) {
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

    public String shortInfo(final User user) {
        String result = discordClient.getUserByID(user.getDiscordId()).getName();
        result += " (" + user.getElo() + ")";
        return result;
    }

    public String getCommandList() {
        return commandList;
    }

    public void setCommandList(final String commandList) {
        this.commandList = commandList;
    }
}
