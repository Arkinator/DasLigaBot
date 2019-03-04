package com.planed.ctlBot.commands;

import com.planed.ctlBot.commands.data.DiscordMessage;
import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.discord.DiscordCommand;
import com.planed.ctlBot.discord.DiscordController;
import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.utils.StringConstants;
import org.springframework.beans.factory.annotation.Autowired;

@DiscordController
public class BasicCommands {
    private static final String CODE_ESCAPE = "```";
    private final DiscordService discordService;

    @Autowired
    public BasicCommands(final DiscordService discordService) {
        this.discordService = discordService;
    }

    @DiscordCommand(name = "hello", help = "Hello World command")
    public void helloCommand(final DiscordMessage call) {
        discordService.whisperToUser(call.getDiscordUser(), "he yourself");
    }

    @DiscordCommand(name = {"list", "help", "commands"}, help = "Lists all available commands")
    public void listAllCommands(final DiscordMessage call) {
        discordService.whisperToUser(call.getDiscordUser(), CODE_ESCAPE + "\n" + StringConstants.HELP_STRING + CODE_ESCAPE);
    }

    @DiscordCommand(name = {"info"}, help = "Displays some information about me!")
    public void infoCommand(final DiscordMessage call) {
        discordService.whisperToUser(call.getDiscordUser(), StringConstants.INFO_STRING);
    }

    @DiscordCommand(name = {"intro"}, help = "Administrator command to introduce the bot to a channel", roleRequired = AccessLevel.ADMIN)
    public void introductionCommand(final DiscordMessage call) {
        discordService.replyInChannel(call.getTextChannel(), StringConstants.INFO_STRING);
    }

    @DiscordCommand(name = {"invite"}, help = "Retrieve the bot invite-link", roleRequired = AccessLevel.USER)
    public void displayInviteLink(final DiscordMessage call) {
        discordService.whisperToUser(call.getDiscordUser(),
                "Invite link for the bot: " + discordService.getInviteLink());
    }
}
