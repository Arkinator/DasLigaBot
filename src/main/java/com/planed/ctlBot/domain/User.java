package com.planed.ctlBot.domain;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.common.League;
import com.planed.ctlBot.common.LigaConstants;
import com.planed.ctlBot.common.Race;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "USER_TABLE")
public class User {
    @Id
    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String discordId;
    @Column(nullable = false)
    private int numberOfInteractions = 0;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel = AccessLevel.USER;
    @Column
    @Enumerated(EnumType.STRING)
    private Race race;
    @Column
    @Enumerated(EnumType.STRING)
    private League league;
    @Column(nullable = false)
    private Double elo = LigaConstants.INITIAL_ELO;
    @Column
    private Long matchId;
    @Column
    private String loginAuthorizationCode;
    @Column
    private String battleNetId;
    @Column
    private String battleNetTokenValue;
    @Column
    private OffsetDateTime battleNetLastUpdate;

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
