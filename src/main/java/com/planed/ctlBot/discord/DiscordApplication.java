package com.planed.ctlBot.discord;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.utils.DiscordMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.Status;

import java.util.Optional;

/**
 * Created by Julian Peters on 23.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
@Transactional
public class DiscordApplication {
    Logger LOG = LoggerFactory.getLogger(DiscordApplication.class);

    private final IDiscordClient discordClient;
    private final CommandRegistry commandRegistry;
    private final DiscordService discordService;
    private final DiscordMessageParser discordMessageParser;

    @Autowired
    public DiscordApplication(
            final CommandRegistry commandRegistry,
            final DiscordService discordService,
            final IDiscordClient discordClient,
            final DiscordMessageParser discordMessageParser) {
        this.discordService = discordService;
        this.discordClient = discordClient;
        this.commandRegistry = commandRegistry;
        this.discordMessageParser = discordMessageParser;
        registerClient();
    }

    private void registerClient() {
        discordClient.getDispatcher().registerListener(this);
        discordService.setDiscordClient(discordClient);
    }

    @EventSubscriber
    public void receiveMessage(final MessageReceivedEvent event) {
        LOG.debug("received event: " + event);

        final Optional<CommandCall> commandCallOptional = discordMessageParser.deconstructMessage(event);
        if (commandCallOptional.isPresent()) {
            processBotCommand(commandCallOptional.get());
        }
    }

    @EventSubscriber
    public void signOn(final ReadyEvent readyEvent) {
        for (final IChannel channel : discordClient.getChannels(false)) {
            LOG.info(channel.getID() + " => " + channel.getGuild().getName() + "." + channel.getName());
        }
    }

    @Scheduled(fixedRate = 5000)
    public void checkDiscordConnection() {
        if (!discordClient.isReady()) {
/*            try {
                discordClient.logout();
                discordClient.login();
            } catch (final DiscordException e) {
                LOG.error("Error while reconnecting: ", e);
            } catch (final HTTP429Exception e) {
                LOG.error("Error while reconnecting: ", e);
            }*/
        }
    }

    @Scheduled(fixedRate = 62020)
    public void updateStatus() {
        discordClient.changeStatus(Status.game("Type !info for awesomness"));
        try {
            discordClient.changeUsername("DAS Liga Bot");
        } catch (final Exception e) {
            LOG.warn("Error on changing status");
        }
    }

    private void processBotCommand(final CommandCall commandCall) {
        commandRegistry.fireEvent(commandCall);
    }
}
