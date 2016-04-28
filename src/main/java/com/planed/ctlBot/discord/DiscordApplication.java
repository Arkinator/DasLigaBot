package com.planed.ctlBot.discord;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.utils.DiscordMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sx.blah.discord.api.EventSubscriber;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import java.util.Optional;

/**
 * Created by Julian Peters on 23.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
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
    public void onReadyEvent(final MessageReceivedEvent event) {
        LOG.debug("received event: " + event);

        final Optional<CommandCall> commandCallOptional = discordMessageParser.deconstructMessage(event);
        if (commandCallOptional.isPresent()) {
            processBotCommand(commandCallOptional.get());
        }
    }

    private void processBotCommand(final CommandCall commandCall) {
        commandRegistry.fireEvent(commandCall);
    }
}
