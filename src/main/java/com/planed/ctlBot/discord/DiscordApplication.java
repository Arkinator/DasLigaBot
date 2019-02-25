package com.planed.ctlBot.discord;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.utils.DiscordMessageParser;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class DiscordApplication {
    private static final Logger logger = LoggerFactory.getLogger(DiscordApplication.class);

    @Autowired
    private DiscordApi discordApi;
    @Autowired
    private DiscordService discordService;
    @Autowired
    private DiscordMessageParser discordMessageParser;

    /*
    @EventSubscriber
    public void signOn(final ReadyEvent readyEvent) {
        for (final IChannel channel : discordClient.getChannels(false)) {
            LOG.info(channel.getStringID() + " => " + channel.getGuild().getName() + "." + channel.getName());
        }
    }*/

/*    @Scheduled(fixedRate = 5000)
    public void checkDiscordConnection() {
        if (!discordClient.isReady()) {
/*            try {
                discordClient.logout();
                discordClient.login();
            } catch (final DiscordException e) {
                LOG.error("Error while reconnecting: ", e);
            } catch (final HTTP429Exception e) {
                LOG.error("Error while reconnecting: ", e);
            }
        }
    }*/
/*
    @Scheduled(fixedRate = 62020)
    public void updateStatus() {
        discordClient.changePresence(StatusType.ONLINE, ActivityType.PLAYING,"Type !info for awesomness");
        try {
            discordClient.changeUsername("DAS Liga Bot");
        } catch (final Exception e) {
            LOG.warn("Error on changing status");
        }
    }*/
}
