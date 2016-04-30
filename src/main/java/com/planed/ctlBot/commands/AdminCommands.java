package com.planed.ctlBot.commands;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.discord.DiscordCommand;
import com.planed.ctlBot.discord.DiscordController;
import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;

@DiscordController
public class AdminCommands {
    @Autowired
    private DiscordService discordService;
    @Autowired
    private MatchRepository matchRepository;

    @DiscordCommand(name = {"matches"}, help = "Displays all matches", roleRequired = AccessLevel.Author)
    public void displayAllMatches(final CommandCall call) {
        final StringBuilder builder = new StringBuilder();
        matchRepository.findAll().forEach(m->builder.append(m.toString()+"\n"));
        discordService.whisperToUser(call.getAuthor().getDiscordId(), builder.toString());
    }
}
