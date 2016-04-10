package com.planed.ctlBot;

import com.planed.ctlBot.commands.BotCommandParser;
import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

@Component
public class CtlBot {
    private final UserService userService;
    Logger LOG = LoggerFactory.getLogger(CtlBot.class);

    private final IDiscordClient client;

    private String discordUsername;
    private String discordPassword;
    private BotCommandParser botCommandParser;

    @Autowired
    public CtlBot(BotCommandParser botCommandParser,
                  @Value("${discord.username}")String discordUsername,
                  @Value("${discord.password}")String discordPassword,
                  UserService userService) {
        this.botCommandParser = botCommandParser;
        this.discordPassword = discordPassword;
        this.discordUsername = discordUsername;
        this.client = getClient();
        this.userService = userService;
        registerClient();
    }

    private void registerClient() {
        // promote fustup :)
        userService.giveUserAccessLevel("<@116296552204599298>", AccessLevel.Author);
        client.getDispatcher().registerListener(new HelloWorldCommandListener(botCommandParser));
        client.getDispatcher().registerListener(new LogOnListener());
    }

    private IDiscordClient getClient() {
        final ClientBuilder clientBuilder = new ClientBuilder();
        LOG.info("logging in with '"+discordUsername+"' and '"+discordPassword+"'");
        if (discordUsername== null || discordPassword== null)
            throw new UnsetUserCredentialsException();
        clientBuilder.withLogin(discordUsername, discordPassword);
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

    private class UnsetUserCredentialsException extends RuntimeException {
    }
}
