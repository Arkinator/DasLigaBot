package com.planed.ctlBot.commands;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sx.blah.discord.api.EventSubscriber;
import sx.blah.discord.handle.impl.events.InviteReceivedEvent;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
public class Backup {
    private final UserService userService;
    Logger LOG = LoggerFactory.getLogger(Backup.class);

    @Autowired
    public Backup(UserService userService) {
        this.userService = userService;
    }

    @EventSubscriber
    public void messageReceiver(MessageReceivedEvent message) {
        LOG.debug("Message received: '"+message.getMessage().getContent());
    }

    @EventSubscriber
    public void followAllInvites(InviteReceivedEvent invite) {
        LOG.debug("Invite received: '"+ invite.getMessage().getContent());
        try {
            String newChannelId = invite.getInvite().accept().getChannelID();
            IGuild newGuild = invite.getClient().getChannelByID(newChannelId).getGuild();
            logAllUsersWithRole(newGuild);
        } catch (DiscordException | HTTP429Exception e) {
            e.printStackTrace();
        }
    }

    @EventSubscriber
    public void helloWorldMessage(ReadyEvent event) {
        event.getClient().getGuilds().forEach(this::logAllUsersWithRole);
    }

    private void logAllUsersWithRole(IGuild newGuild) {
        for (IUser user : newGuild.getUsers()) {
            LOG.info("\t"+user.getName()+" ("+user.getID()+") "+user.getRolesForGuild(newGuild));
            if (user.getRolesForGuild(newGuild).contains("Admin")){
                userService.giveUserAccessLevel("<@"+user.getID()+">", AccessLevel.Admin);
            }
        }
    }
}
