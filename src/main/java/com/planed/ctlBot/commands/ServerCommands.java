package com.planed.ctlBot.commands;

import com.planed.ctlBot.commands.data.DiscordMessage;
import com.planed.ctlBot.discord.DiscordCommand;
import com.planed.ctlBot.discord.DiscordController;
import com.planed.ctlBot.discord.DiscordServerJoinEvent;
import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.services.ServerService;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.ServerJoinEvent;
import org.springframework.beans.factory.annotation.Autowired;

@DiscordController
public class ServerCommands {
    private static final String SERVER_OWNER_WELCOME_MESSAGE = "Hello! serverowner! welcome!";

    @Autowired
    private DiscordService discordService;
    @Autowired
    private ServerService serverService;

    @DiscordServerJoinEvent
    public void onServerJoin(ServerJoinEvent serverJoinEvent) {
        discordService.whisperToUser(serverJoinEvent.getServer().getOwner(), SERVER_OWNER_WELCOME_MESSAGE);

        serverService.findOrCreateServer(serverJoinEvent.getServer());
    }

    @DiscordCommand(name = {"setAnnouncerChannel"},
            help = "Change the announcer channel for a server. Must link the announcer channel directly. Can only be invoked by server owner.",
            minChannelLinks = 1)
    public void setAnnouncerChannel(final DiscordMessage call) {
        if (call.isPrivateMessage()) {
            discordService.whisperToUser(call.getDiscordUser(), "This command can only be called on a server. You must link the channel which to promote!");
            return;
        }

        final Server server = call.getMessage().getServer().get();
        final User serverOwner = server.getOwner();
        if (!serverOwner.equals(call.getDiscordUser())) {
            discordService.whisperToUser(call.getDiscordUser(), "This command can only be called by the server owner!");
            return;
        }

        final ServerTextChannel targetChannel = call.getMentionedChannels().get(0);
        serverService.setServerAnnouncementChannel(server, targetChannel);

        discordService.replyInChannel(call.getTextChannel(), "Announcement channel changed to <#" + targetChannel.getIdAsString() + ">");
    }

    @DiscordCommand(name = {"setPrefix"}, help = "Change the command prefix for this server. Can only be invoked by server owner.", minParameters = 1)
    public void setServerPrefix(final DiscordMessage call) {
        if (call.isPrivateMessage()) {
            discordService.whisperToUser(call.getDiscordUser(), "This command can only be called on a server.");
            return;
        }

        final Server server = call.getMessage().getServer().get();
        final User serverOwner = server.getOwner();
        if (!serverOwner.equals(call.getDiscordUser())) {
            discordService.whisperToUser(call.getDiscordUser(), "This command can only be called by the server owner!");
            return;
        }

        serverService.setServerPrefix(server, call.getParameters().get(0));

        discordService.replyInChannel(call.getTextChannel(), "Server prefix for this server changed to '" + call.getParameters().get(0) + "'");
    }
}
