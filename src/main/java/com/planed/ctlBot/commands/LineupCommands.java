package com.planed.ctlBot.commands;


import java.util.List;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.discord.DiscordCommand;
import com.planed.ctlBot.discord.DiscordController;
import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.Lineup;
import com.planed.ctlBot.domain.LineupRepository;
import com.planed.ctlBot.services.LineupService;

/**
 * 
 * @author azthec
 *
 */
@DiscordController
public class LineupCommands {
	Logger LOG = LoggerFactory.getLogger(LineupCommands.class);

	@Autowired
	private LineupRepository lineupRepository;
	@Autowired
	private DiscordService discordService;
	@Autowired
	private LineupService lineupService;

	@DiscordCommand(name = {"createmessage"}, help = "Creates lineup - {createlineup, lineupUniqueId, mentions, message}", roleRequired = AccessLevel.Admin)
	public void createLineupCommand(final CommandCall call) {
		if(call.getParameters().size()>=3) {
			StringBuilder message = new StringBuilder();
			for(int i = 2; i<call.getParameters().size(); i++)
				message.append(call.getParameters().get(i) + " ");
			lineupRepository.addLineup(call.getParameters().get(0),call.getParameters().get(1), message.toString().trim());
		} //else error handling report back to user?
	}

	@DiscordCommand(name = {"listmessages"}, help = "Lists available lineups - {listlineups}")
	public void listLineupsCommand(final CommandCall call) {
		List<Lineup> list = lineupService.getAllLineups();
		if(list!=null)
			discordService.replyInChannel(call.getChannel(), lineupService.getAllLineups().size() + "" );
		for(Lineup tmpLineup : list) 
			discordService.replyInChannel(call.getChannel(), tmpLineup.getLineupId() + " " 
					+ tmpLineup.getMessage());
	}

	@DiscordCommand(name = {"editmessage"}, help = "Sets lineup message - {setlineupmessage, lineupUniqueId, message}", roleRequired = AccessLevel.Admin)
	public void setLineupMessageCommand(final CommandCall call) {
		Lineup lineup = lineupRepository.findByLineupId(call.getParameters().get(0));
		StringBuilder message = new StringBuilder();
		for(int i = 1; i<call.getParameters().size(); i++)
			message.append(call.getParameters().get(i) + " ");
		if(lineup!=null) {
			lineup.setMessage(message.toString());
		}
	}

	@DiscordCommand(name = {"editmessagementions"}, help = "Sets lineup mentions - {setlineupmentions, lineupUniqueId, new mention tag}", roleRequired = AccessLevel.Admin)
	public void setLineupMentionCommand(final CommandCall call) {
		Lineup lineup = lineupRepository.findByLineupId(call.getParameters().get(0));
		if(lineup!=null) {
			lineup.setPlayerMentions(call.getParameters().get(1));
		}
	}

	@DiscordCommand(name = {"deletemessage"}, help = "deletes named lineup - {deletelineup, lineupUniqueId}", roleRequired = AccessLevel.Admin)
	public void deleteLineupCommand(final CommandCall call) {
		Lineup lineup = lineupRepository.findByLineupId(call.getParameters().get(0));
		if(lineup!=null) {
			lineupRepository.removeLineup(call.getParameters().get(0));
		}
	}

	@DiscordCommand(name = {"messagewmentions"}, help = "lists named lineup with mentions - {lineupwmentions, lineupUniqueId}", roleRequired = AccessLevel.Admin)
	public void listLineupWithoutMentionCommand(final CommandCall call) {
		Lineup lineup = lineupRepository.findByLineupId(call.getParameters().get(0));
		discordService.replyInChannel(call.getChannel(), lineup.getPlayerMentions() + " "
				+ lineup.getMessage());
	}

	@DiscordCommand(name = {"message"}, help = "lists named lineup - {lineup, lineupUniqueId}")
	public void listLineupCommand(final CommandCall call) {
		Lineup lineup = lineupRepository.findByLineupId(call.getParameters().get(0));
		discordService.replyInChannel(call.getChannel(), "" + lineup.getMessage());
	}
}
