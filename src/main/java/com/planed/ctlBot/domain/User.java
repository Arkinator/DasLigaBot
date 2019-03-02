package com.planed.ctlBot.domain;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.common.LigaConstants;
import com.planed.ctlBot.common.Race;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @EqualsAndHashCode.Include
    private String discordId;
    private int numberOfInteractions = 0;
    private AccessLevel accessLevel = AccessLevel.USER;
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

    public String toString() {
        return "<@" + getDiscordId() + ">";
    }

    public String getInfo() {
        return "<@" + getDiscordId() + "> (" + getElo() + " ELO, " + getNumberOfInteractions() + " interactions)";
    }
}
