package com.planed.ctlBot.domain;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.common.Race;

public class User {
    private long userId;

    private String discordId;

    private int numberOfInteractions;

    private AccessLevel accessLevel;

    private Race race;

    private Match match;

    private Double elo;

    public User() {
    }

    public User(final long userId, final String discordId, final int numberOfInteractions, final AccessLevel accessLevel) {
        this.userId = userId;
        this.discordId = discordId;
        this.numberOfInteractions = numberOfInteractions;
        this.accessLevel = accessLevel;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(final long userId) {
        this.userId = userId;
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

    public Match getMatch() {
        return match;
    }

    public void setMatch(final Match match) {
        this.match = match;
    }

    public Double getElo() {
        return elo;
    }

    public void setElo(final Double elo) {
        this.elo = elo;
    }
}
