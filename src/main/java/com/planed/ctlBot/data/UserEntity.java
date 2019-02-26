package com.planed.ctlBot.data;


import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.common.LigaConstants;
import com.planed.ctlBot.common.Race;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
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
}
