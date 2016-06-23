package com.planed.ctlBot.commands;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.discord.DiscordCommand;
import com.planed.ctlBot.discord.DiscordController;
import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.Lineup;
import com.planed.ctlBot.domain.LineupRepository;

/**
 * 
 * @author azthec
 *
 */
@DiscordController
public class LineupCommands {
	Logger LOG = LoggerFactory.getLogger(UserCommands.class);

	@Autowired
	private LineupRepository lineupRepository;
	@Autowired
	private DiscordService discordService;

	@DiscordCommand(name = {"createlineup"}, help = "Creates lineup - {createlineup, lineupUniqueId, mentions, message}")
	public void createLineupCommand(final CommandCall call) {
		if(call.getParameters().size()>=3) {
			StringBuilder message = new StringBuilder();
			for(int i = 2; i<call.getParameters().size(); i++)
				message.append(call.getParameters().get(i) + " ");
			lineupRepository.addLineup(call.getParameters().get(0),call.getParameters().get(1), message.toString().trim());
		} //else error handling report back to user?
	}

	@DiscordCommand(name = {"listlineups"}, help = "Lists available lineups - {listlineups}")
	public void listLineupsCommand(final CommandCall call) {
		List<Lineup> list = lineupRepository.findAll();
		for(int i = 0; i<list.size();i++)
			discordService.replyInChannel(call.getChannel(), list.get(i).getLineupId() + " " 
					+ list.get(i).getPlayerMentions() + " "
					+ list.get(i).getMessage());
	}

	@DiscordCommand(name = {"setlineupmessage"}, help = "Sets lineup message - {setlineupmessage, lineupUniqueId, message}")
	public void setLineupMessageCommand(final CommandCall call) {
		Lineup lineup = lineupRepository.findByLineupId(call.getParameters().get(0));
		if(lineup!=null) {
			lineup.setMessage(call.getParameters().get(1));
		}
	}
	
	@DiscordCommand(name = {"setlineupmentions"}, help = "Sets lineup mentions - {setlineupmentions, lineupUniqueId, new mention tag}")
	public void setLineupMentionCommand(final CommandCall call) {
		Lineup lineup = lineupRepository.findByLineupId(call.getParameters().get(0));
		if(lineup!=null) {
			lineup.setPlayerMentions(call.getParameters().get(1));
		}
	}
	
	@DiscordCommand(name = {"deletelineup"}, help = "deletes named lineup - {deletelineup, lineupUniqueId}")
	public void deleteLineupCommand(final CommandCall call) {
		Lineup lineup = lineupRepository.findByLineupId(call.getParameters().get(0));
		if(lineup!=null) {
			lineupRepository.removeLineup(call.getParameters().get(0));
		}
	}
}
