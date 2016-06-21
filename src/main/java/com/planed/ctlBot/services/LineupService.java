package com.planed.ctlBot.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.planed.ctlBot.common.Race;
import com.planed.ctlBot.data.repositories.LineupEntityRepository;
import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.Lineup;
import com.planed.ctlBot.domain.LineupRepository;
import com.planed.ctlBot.domain.Match;
import com.planed.ctlBot.domain.MatchRepository;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.domain.UserRepository;

/**
 * 
 * @author azthec
 *
 */
@Component
public class LineupService {
	private final LineupRepository lineupRepository;

	@Autowired
	public LineupService(final LineupRepository lineupRepository) {
		this.lineupRepository = lineupRepository;
	}

	public List<Lineup> getAllLineups() {
		final List<Lineup> result = new ArrayList<Lineup>();
		lineupRepository.findAll().forEach(result::add);
		return result;
	}

	public Lineup findLineupAndCreateIfNotFound(final String lineupId) {
		Lineup result = lineupRepository.findByLineupId(lineupId);
		if (result == null) {
			addNewLineup(lineupId);
			result = lineupRepository.findByLineupId(lineupId);
		}
		return result;
	}

	public void addNewLineup(final String lineupId) {
		Lineup result = new Lineup(lineupId);
		lineupRepository.save(result);
	}

	public void changeMessage(final Lineup lineup, final String newMessage) {
		lineup.setMessage(newMessage);
		lineupRepository.save(lineup);
	}

	public void changeMention(final Lineup lineup, final String newMentions) {
		lineup.setPlayerMentions(newMentions);
		lineupRepository.save(lineup);
	}
	
	private void removeLineup(final String lineupId) {
		lineupRepository.removeLineup(lineupId);
	}
}
