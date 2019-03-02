package com.planed.ctlBot.services;

import com.planed.ctlBot.data.DiscordServer;
import com.planed.ctlBot.data.repositories.ServerRepository;
import org.javacord.api.entity.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServerService {
    @Autowired
    private ServerRepository serverRepository;

    public void addServer(Server server) {
        if (serverRepository.findById(server.getId()).isPresent()) {
            return;
        }

        serverRepository.save(DiscordServer.builder()
                .serverId(server.getId())
                .announcerChannelId(server.g)
                .build())
    }
}
