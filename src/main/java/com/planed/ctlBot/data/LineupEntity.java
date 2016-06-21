package com.planed.ctlBot.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author azthec
 *
 */
@Entity
@Table(name = "LINEUPS_TABLE")
public class LineupEntity { //this defines db structure for this table
	@Id
	private String lineupId;
	@Column
	private String playerMentions;
	@Column
	private String message;
	
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
