package com.planed.ctlBot.domain;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.common.LigaConstants;
import com.planed.ctlBot.common.Race;

public class User {
    private String discordId;

    private int numberOfInteractions = 0;

    private AccessLevel accessLevel = AccessLevel.User;

    private Race race;

    private Long matchId;

    private Double elo = LigaConstants.INITIAL_ELO;

    public User() {
    }

    public User(final String discordId, final int numberOfInteractions, final AccessLevel accessLevel) {
        this.discordId = discordId;
        this.numberOfInteractions = numberOfInteractions;
        this.accessLevel = accessLevel;
    }

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(final String discordId) {
        this.discordId = discordId;
    }

    public int getNumberOfInteractions() {
        return numberOfInteractions;
    }

    public void setNumberOfInteractions(final int numberOfInteractions) {
        this.numberOfInteractions = numberOfInteractions;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(final AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(final Race race) {
        this.race = race;
    }

    public Double getElo() {
        return elo;
    }

    public void setElo(final Double elo) {
        this.elo = elo;
    }

    public String toString() {
        return "<@" + getDiscordId() + ">";
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(final Long matchId) {
        this.matchId = matchId;
    }

    public String getInfo() {
        return "<@" + getDiscordId() + "> (" + getElo() + " ELO, " + getNumberOfInteractions() + " interactions)";
    }
}
