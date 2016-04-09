package com.planed.ctlBot;

import com.planed.ctlBot.commands.BotCommandParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

@Component
public class CtlBot {
    Logger LOG = LoggerFactory.getLogger(CtlBot.class);

    private final IDiscordClient client;

    BotCommandParser botCommandParser;
    @Autowired
    public CtlBot(BotCommandParser botCommandParser) {
        this.botCommandParser = botCommandParser;
        this.client = getClient();
        registerClient();
    }

    private void registerClient() {
        client.getDispatcher().registerListener(new HelloWorldCommandListener(botCommandParser));
        client.getDispatcher().registerListener(new LogOnListener());
    }

    private IDiscordClient getClient() {
        final ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withLogin("j_ulianpeters@gmx.de", "al3DsplDSIn47GZg7FKK");
        try {
            return clientBuilder.login();
        } catch (DiscordException e) {
            throw new DiscordLoginException(e);
        }
    }

    private static class DiscordLoginException extends RuntimeException {
        public DiscordLoginException(DiscordException e) {
            super(e);
        }
    }
}
