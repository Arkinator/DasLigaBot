package com.planed.ctlBot;


import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"sx.blah.discord", "com.planed.ctlBot"})
public class BotBoot {
    @Value("${discord.token}")
    private String token;

    public static void main(final String[] args) {
        SpringApplication.run(BotBoot.class, args);
    }

    @Bean
    public DiscordApi discordApi() {
        return new DiscordApiBuilder().setToken(token).login().join();
    }
}
