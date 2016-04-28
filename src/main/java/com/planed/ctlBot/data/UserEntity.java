package com.planed.ctlBot.data;


import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.common.LigaConstants;
import com.planed.ctlBot.common.Race;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Entity
@Table(name="USER_TABLE")
public class UserEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long userId;
    @Column(nullable = false)
    private String discordId;
    @Column(nullable = false)
    private int numberOfInteractions = 0;
    @Column(nullable = false)
    private AccessLevel accessLevel = AccessLevel.User;
    @Column
    private Race race;
    @Column(nullable = false)
    private final Double elo = LigaConstants.INITIAL_ELO;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "matchId")
    private MatchEntity match;

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(final String discordId) {
        this.discordId = discordId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(final long userId) {
        this.userId = userId;
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

    public MatchEntity getMatch() {
        return match;
    }

    public void setMatch(final MatchEntity match) {
        this.match = match;
    }
}
