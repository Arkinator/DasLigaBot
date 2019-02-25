package com.planed.ctlBot.commands;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.discord.DiscordCommand;
import com.planed.ctlBot.discord.DiscordController;
import com.planed.ctlBot.discord.DiscordService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

@DiscordController
public class BasicCommands {
    private static final String CODE_ESCAPE = "```";
    Logger LOG = LoggerFactory.getLogger(BasicCommands.class);

    private final DiscordService discordService;
    private String infoString = "error booting the server";
    private String helpString = "error booting the server";

    @Autowired
    public BasicCommands(final DiscordService discordService) {
        this.discordService = discordService;
        try {
            this.infoString = FileUtils.readFileToString(new File(this.getClass().getClassLoader().getResource("logo.txt").getFile()));
            this.helpString = FileUtils.readFileToString(new File(this.getClass().getClassLoader().getResource("help.txt").getFile()));
        } catch (final IOException e) {
            LOG.error("Error while reading file:", e);
        }
    }

    @DiscordCommand(name = "hello", help = "Hello World command")
    public void helloCommand(final CommandCall call) {
        discordService.whisperToUser(call.getAuthor().getDiscordId(), "he yourself");
    }

    @DiscordCommand(name = {"list", "help", "commands"}, help = "Lists all available commands")
    public void listAllCommands(final CommandCall call) {
        discordService.whisperToUser(call.getAuthor().getDiscordId(), CODE_ESCAPE + "\n" + helpString + CODE_ESCAPE);
    }

    @DiscordCommand(name = {"info"}, help = "Displays some information about me!")
    public void infoCommand(final CommandCall call) {
        discordService.whisperToUser(call.getAuthor().getDiscordId(), buildInfoText(call));
    }

    @DiscordCommand(name = {"intro"}, help = "Administrator command to introduce the bot to a channel", roleRequired = AccessLevel.Admin)
    public void introductionCommand(final CommandCall call) {
        discordService.replyInChannel(call.getChannel(), buildInfoText(call));
    }

    private String buildInfoText(final CommandCall call) {
        return CODE_ESCAPE + "\n" + infoString + CODE_ESCAPE;
    }
}
