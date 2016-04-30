package com.planed.ctlBot;


import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.scheduling.annotation.EnableScheduling;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.EventDispatcher;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;

import javax.sql.DataSource;

import static org.hibernate.jpa.internal.QueryImpl.LOG;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * Created by jules on 09.04.2016.
 */
@Configuration
@ComponentScan(basePackages = "com.planed.ctlBot")
@PropertySource("/application.properties")
@EnableAutoConfiguration
@EnableScheduling
public class BotBoot {
    @Value("${discord.username}")
    private String discordUsername;
    @Value("${discord.password}")
    private String discordPassword;

    public static void main(final String[] args) {
//        System.setProperty("derby.system.homeSystem.setProp", "/Users/jps/code/CtlBattleBot/");
        SpringApplication.run(BotBoot.class, args);
    }

    @Bean
    @Profile("!development")
    public DataSource dbFromFileSystem() {
        final DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
        ds.setUrl("jdbc:derby:/Users/jps/code/CtlBattleBot/database.db");
        ds.setUsername("");
        ds.setPassword("");
        return ds;
    }

    @Bean
    @Profile("development")
    public DataSource inMemoryDb() {
        final EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder
                .setType(EmbeddedDatabaseType.DERBY)
                .build();
    }

    @Bean(name = "discordClient")
    @Profile("!development")
    public IDiscordClient realClient() {
        final ClientBuilder clientBuilder = new ClientBuilder();
        LOG.info("logging in with '" + discordUsername + "' and '" + discordPassword + "'");
        if (discordUsername == null || discordPassword == null) {
            throw new UnsetUserCredentialsException();
        }
        clientBuilder.withLogin(discordUsername, discordPassword);
        try {
            return clientBuilder.login();
        } catch (final DiscordException e) {
            throw new DiscordLoginException(e);
        }
    }

    @Bean(name = "discordClient")
    @Profile("development")
    public IDiscordClient mockClient() {
        try {
            final IDiscordClient result = mock(IDiscordClient.class);
            Mockito.when(result.getDispatcher()).thenReturn(mock(EventDispatcher.class));
            Mockito.when(result.getChannelByID(any())).thenReturn(mock(IChannel.class));
            Mockito.when(result.getOrCreatePMChannel(any())).thenReturn(mock(IPrivateChannel.class));
            Mockito.when(result.getUserByID(any())).thenReturn(mock(IUser.class));

            return result;
        } catch (final Exception e ){
            return null;
        }
    }

    private static class DiscordLoginException extends RuntimeException {
        public DiscordLoginException(final DiscordException e) {
            super(e);
        }
    }

    private class UnsetUserCredentialsException extends RuntimeException {
    }
}
