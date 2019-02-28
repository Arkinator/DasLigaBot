package com.planed.ctlBot.commands;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.discord.DiscordCommand;
import com.planed.ctlBot.discord.DiscordController;
import com.planed.ctlBot.discord.DiscordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;

import java.time.Duration;

@DiscordController
public class AuthorCommands {
    private static final Logger logger = LoggerFactory.getLogger(AuthorCommands.class);
    @Autowired
    private DiscordService discordService;
    @Autowired
    private MetricsEndpoint metricsEndpoint;

    @DiscordCommand(name = {"serverStatus"}, help = "Display server status", roleRequired = AccessLevel.AUTHOR)
    public void displayServerStatus(final CommandCall call) {
        discordService.whisperToUser(call.getAuthor().getDiscordId(),
                "IP-Address: " + getIpAddress() + "\n" +
                        "Uptime: " + getUptime());
    }

    private String getUptime() {
        double uptimeInSeconds = metricsEndpoint.metric("process.uptime", null).getMeasurements().get(0).getValue();
        return Duration.ofSeconds((long) uptimeInSeconds).toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    private String getIpAddress() {
        try {
            return Unirest.get("http://ipv4bot.whatismyipaddress.com").asString().getBody();
        } catch (UnirestException e) {
            logger.warn("Error while querying IP address: ", e);
            return "<Error while querying IP address>";
        }
    }
}
