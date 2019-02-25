package com.planed.ctlBot.data;


import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.common.LigaConstants;
import com.planed.ctlBot.common.Race;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USER_TABLE")
public class UserEntity {
    @Id
    @Column(nullable = false)
    private String discordId;
    @Column(nullable = false)
    private int numberOfInteractions = 0;
    @Column(nullable = false)
    private AccessLevel accessLevel = AccessLevel.User;
    @Column
    private Race race;
    @Column(nullable = false)
    private Double elo = LigaConstants.INITIAL_ELO;
    @Column
    private Long matchId;

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(final String discordId) {
        this.discordId = discordId;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(final AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public int getNumberOfInteractions() {
        return numberOfInteractions;
    }

    public void setNumberOfInteractions(final int numberOfInteractions) {
        this.numberOfInteractions = numberOfInteractions;
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

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(final Long matchId) {
        this.matchId = matchId;
    }

    public void setElo(final Double elo) {
        this.elo = elo;
    }
}
