package com.planed.ctlBot.domain;

/**
 * 
 * @author azthec
 *
 */
public class Lineup { //defines object lineup (in memory)
	private String lineupId;
	private String playerMentions;
	private String message;

	public Lineup() { //fucking dumb dozer needs parameterless constructors
	}

	public Lineup(final String lineupId) {
		this.lineupId = lineupId;
	}

	public Lineup(final String lineupId, String playerMentions, String message) {
		this.lineupId = lineupId;
		this.playerMentions = playerMentions;
		this.message = message;
	}

	public String getLineupId() {
		return lineupId;
	}

	public void setLineupId(final String lineupId) { //final so it can't change value after its set
		this.lineupId = lineupId;
	}

	public String getPlayerMentions() {
		return playerMentions;
	}

	public void setPlayerMentions(String playerMentions) {
		this.playerMentions = playerMentions;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
