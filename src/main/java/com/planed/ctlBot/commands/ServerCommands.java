package com.planed.ctlBot.commands;

import com.planed.ctlBot.discord.DiscordController;
import com.planed.ctlBot.discord.DiscordCustomEvent;
import com.planed.ctlBot.discord.DiscordEventType;
import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.services.ServerService;
import org.javacord.api.event.server.ServerJoinEvent;
import org.springframework.beans.factory.annotation.Autowired;

@DiscordController
public class ServerCommands {
    private static final String SERVER_OWNER_WELCOME_MESSAGE = "Hello! serverowner! welcome!";

    @Autowired
    private DiscordService discordService;
    @Autowired
    private ServerService serverService;

    @DiscordCustomEvent(eventType = DiscordEventType.SERVER_JOIN_EVENT)
    private void onServerJoin(ServerJoinEvent serverJoinEvent) {
        discordService.whisperToUser(serverJoinEvent.getServer().getOwner(), SERVER_OWNER_WELCOME_MESSAGE);

        serverService.addServer(serverJoinEvent.getServer());
    }
}
