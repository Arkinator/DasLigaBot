package com.planed.ctlBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MissingPermissionsException;

/**
 * Created by jules on 09.04.2016.
 */
public class InterfaceListener {
    Logger LOG = LoggerFactory.getLogger(InterfaceListener.class);

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) { //This method is called when the ReadyEvent is dispatched
        LOG.info(event.toString());
        LOG.info(event.getClass().getCanonicalName());
        for (IGuild guild:event.getClient().getGuilds()) {
            LOG.info("GUILD:"+guild.getName());
        }
        for (IChannel channel:event.getClient().getChannels(true)) {
            LOG.info("CHANNEL:"+channel.getName());
            try {
                channel.sendMessage("Hello World!");
            } catch (MissingPermissionsException e) {
                e.printStackTrace();
            } catch (HTTP429Exception e) {
                e.printStackTrace();
            } catch (DiscordException e) {
                e.printStackTrace();
            }
        }
    }

    @EventSubscriber
    public void onReadyEvent(MessageReceivedEvent message) { //This method is called when the ReadyEvent is dispatched
        LOG.info("message logged: "+message.getMessage().getContent());
    }
}
