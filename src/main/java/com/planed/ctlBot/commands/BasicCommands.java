package com.planed.ctlBot.commands;

import com.planed.ctlBot.commands.data.DiscordMessage;
import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.discord.DiscordCommand;
import com.planed.ctlBot.discord.DiscordController;
import com.planed.ctlBot.discord.DiscordService;
import org.springframework.beans.factory.annotation.Autowired;

@DiscordController
public class BasicCommands {
    private static final String CODE_ESCAPE = "```";
    private final DiscordService discordService;

    public static final String infoString = "```\n" +
            "----------------------------------------------------------------------\n" +
            "*    __________________ .___.___                                       *\n" +
            "*   /   _____/\\_   ___ \\|   |   |      Starcraft 2 - DAS Liga          *\n" +
            "*   \\_____  \\ /    \\  \\/|   |   |                                      *\n" +
            "*   /        \\\\     \\___|   |   |   !hello, I'm a Discord-Bot.         *\n" +
            "*  /_______  / \\______  /___|___|   I run an automated starcraft       *\n" +
            "*          \\/         \\/            tournament. Just type !challenge   *\n" +
            "*   (D) iscord            to challenge anybody to play. The other      *\n" +
            "*   (A) utomated          person has to !accept the challenge. Then    *\n" +
            "*   (S) tarcraft          you battle it out in SCII and !report the    *\n" +
            "*                         result back to me. Every win or loss will    *\n" +
            "*      _     _            gain or loose you ELO points, impacting      *\n" +
            "*     | |   (_)                 your ranking. With !league you can     *\n" +
            "*     | |    _  __ _  __ _      take a look at the current rankings.   *\n" +
            "*     | |   | |/ _` |/ _` |                                            *\n" +
            "*     | |___| | (_| | (_| |         If you have any question, ask for  *\n" +
            "*     \\_____/_|\\__, |\\__,_|         !help                              *\n" +
            "*               __/ |                                                  *\n" +
            "*              |___/                 glhf                              *\n" +
            "*                                                                      *\n" +
            "*                                   DAS Liga Bot                       *\n" +
            "----------------------------------------------------------------------\n```";

    private String helpString = "```-------------------------Help Menu------------------------------------\n" +
            "*\n" +
            "* !league                 This displays the current league standings\n" +
            "* !info                   Displays some information about me!\n" +
            "* !list                   Lists all available commands\n" +
            "* !hello                  Hello World command\n" +
            "*\n" +
            "*           You can challenge people to play\n" +
            "* !challenge              Challenge a player. Just type @ and his name!\n" +
            "* !reject or !swipeleft   Reject your current challenge\n" +
            "* !revoke                 Revoke your current challenge\n" +
            "* !accept or !swiperight  Accept the challenge extended to you!\n" +
            "* !report                 Report either a 'win' or a 'loss' in the current game\n" +
            "* !race                   This command lets you change your race. Do you play Zerg, Terran, Protoss or Random?\n" +
            "* !status                 This displays your current status (open challenges, league position etc)\n" +
            "----------------------------------------------------------------------\n```";

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
        discordService.whisperToUser(call.getDiscordUser(), CODE_ESCAPE + "\n" + helpString + CODE_ESCAPE);
    }

    @DiscordCommand(name = {"info"}, help = "Displays some information about me!")
    public void infoCommand(final DiscordMessage call) {
        discordService.whisperToUser(call.getDiscordUser(), infoString);
    }

    @DiscordCommand(name = {"intro"}, help = "Administrator command to introduce the bot to a channel", roleRequired = AccessLevel.ADMIN)
    public void introductionCommand(final DiscordMessage call) {
        discordService.replyInChannel(call.getTextChannel(), infoString);
    }

    @DiscordCommand(name = {"invite"}, help = "Retrieve the bot invite-link", roleRequired = AccessLevel.USER)
    public void displayInviteLink(final DiscordMessage call) {
        discordService.whisperToUser(call.getDiscordUser(),
                "Invite link for the bot: " + discordService.getInviteLink());
    }
}
