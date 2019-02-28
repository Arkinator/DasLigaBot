package com.planed.ctlBot.commands;

import com.planed.ctlBot.commands.data.DiscordMessage;
import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.discord.DiscordCommand;
import com.planed.ctlBot.discord.DiscordController;
import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.MatchRepository;
import com.planed.ctlBot.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

@DiscordController
public class AdminCommands {
    @Autowired
    private DiscordService discordService;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private UserRepository userRepository;

    @DiscordCommand(name = {"matches"}, help = "Displays all matches", roleRequired = AccessLevel.AUTHOR)
    public void displayAllMatches(final DiscordMessage call) {
        final StringBuilder builder = new StringBuilder();
        matchRepository.findAll().forEach(m -> builder.append(m.toString() + "\n"));
        discordService.whisperToUser(call.getAuthor().getDiscordId(), builder.toString());
    }

    @DiscordCommand(name = {"users"}, help = "Displays all users", roleRequired = AccessLevel.AUTHOR)
    public void displayAllUsers(final DiscordMessage call) {
        final StringBuilder builder = new StringBuilder();
        userRepository.findAll().forEach(p -> builder.append(p.getInfo() + "\n"));
        discordService.whisperToUser(call.getAuthor().getDiscordId(), builder.toString());
    }
}
