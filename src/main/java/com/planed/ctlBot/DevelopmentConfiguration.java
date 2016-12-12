package com.planed.ctlBot;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;

import javax.sql.DataSource;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;


@Configuration
@Profile("development")
public class DevelopmentConfiguration {
    @Bean
    public DataSource inMemoryDb() {
        final EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        DataSource ds = builder
                .setType(EmbeddedDatabaseType.DERBY)
                .build();
        return ds;
    }

/*    @Bean(name = "discordClient")
    public IDiscordClient realClient() {
        final ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken("MTk0NzE1MjU0Nzg0MTMxMDcz.Ckp-Ng.-dtm8JRr1RJTSVuVUdowKJSmsyE");
        try {
            return clientBuilder.login();
        } catch (final DiscordException e) {
            throw new RuntimeException("Error while logging in to Discord", e);
        }
    }*/

    @Bean(name = "discordClient")
    public IDiscordClient mockClient() {
        try {
            final IDiscordClient result = mock(IDiscordClient.class);
            Mockito.when(result.getDispatcher()).thenReturn(mock(EventDispatcher.class));
            Mockito.when(result.getChannelByID(any())).thenReturn(mock(IChannel.class));
            Mockito.when(result.getOrCreatePMChannel(any())).thenReturn(mock(IPrivateChannel.class));
            Mockito.when(result.getUserByID(any())).thenReturn(mock(IUser.class));

            return result;
        } catch (final Exception e) {
            return null;
        }
    }
}
