package com.planed.ctlBot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

import javax.sql.DataSource;


@Configuration
@Profile("!development")
public class StandardConfiguration {
    @Bean
    public DataSource connectToMySql() {
        final DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://inet00.de:3306/k39321h8_DasLiga");
        ds.setUsername("k39321h8_LigaBot");
        ds.setPassword("epLC976XM9bemX5YxeKP");
        return ds;
    }

    @Bean(name = "discordClient")
    public IDiscordClient realClient() {
        final ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken("MTk0NzE1MjU0Nzg0MTMxMDcz.Ckp-Ng.-dtm8JRr1RJTSVuVUdowKJSmsyE");
        try {
            return clientBuilder.login();
        } catch (final DiscordException e) {
            throw new RuntimeException("Error while logging in to Discord", e);
        }
    }
}
