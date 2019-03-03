package com.planed.ctlBot.services;

import com.planed.ctlBot.data.DiscordServer;
import com.planed.ctlBot.data.repositories.ServerRepository;
import com.planed.ctlBot.discord.DiscordService;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ServerService {
    @Autowired
    private ServerRepository serverRepository;
    @Autowired
    private DiscordService discordService;

    public DiscordServer findOrCreateServer(Server server) {
        return serverRepository.findById(server.getId())
                .orElseGet(() -> serverRepository.save(DiscordServer.builder()
                        .serverId(server.getId())
                        .announcerChannelId(server.getTextChannels().get(0).getId())
                        .build()));
    }

    public void setServerAnnouncementChannel(Server server, ServerTextChannel serverTextChannel) {
        DiscordServer discordServer = findOrCreateServer(server);
        discordServer.setAnnouncerChannelId(serverTextChannel.getId());
        serverRepository.save(discordServer);
    }

    public void setServerPrefix(Server server, String prefix) {
        DiscordServer discordServer = findOrCreateServer(server);
        discordServer.setCommandEscaper(prefix);
        serverRepository.save(discordServer);
    }

    public Optional<Long> findAnnouncerChannelIdForServer(Long originatingServerId) {
        final Optional<Long> channelOptional = serverRepository.findById(originatingServerId)
                .map(server -> server.getAnnouncerChannelId());
        if (channelOptional.isPresent()) {
            return channelOptional;
        }

        return findAnnouncerChannelForNewServer(originatingServerId);
    }

    private Optional<Long> findAnnouncerChannelForNewServer(Long originatingServerId) {
        return discordService.findServerById(originatingServerId)
                .map(server -> findOrCreateServer(server))
                .map(server -> server.getAnnouncerChannelId());
    }

    public Optional<String> getServerPrefix(long id) {
        return discordService.findServerById(id)
                .map(server -> findOrCreateServer(server))
                .map(server -> server.getCommandEscaper());
    }
}
