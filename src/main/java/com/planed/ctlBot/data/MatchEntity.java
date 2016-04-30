package com.planed.ctlBot.data;


import com.planed.ctlBot.common.GameStatus;
import com.planed.ctlBot.common.Race;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Entity
@Table(name = "MATCHES")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long matchId;

    @OneToMany(mappedBy= "match", fetch = FetchType.EAGER)
    private List<UserEntity> players;
    @Column(nullable = false)
    private GameStatus gameStatus;
    @Column
    private Integer finalScorePlayerA;
    @Column
    private Integer finalScorePlayerB;
    @Column
    private Race racePlayerA;
    @Column
    private Race racePlayerB;

    @Column
    private Integer playerAreportedScoreForPlayerA;
    @Column
    private Integer playerAreportedScoreForPlayerB;
    @Column
    private Integer playerBreportedScoreForPlayerA;
    @Column
    private Integer playerBreportedScoreForPlayerB;

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(final long matchId) {
        this.matchId = matchId;
    }

    public List<UserEntity> getPlayers() {
        return players;
    }

    public void setPlayers(final List<UserEntity> players) {
        this.players = players;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public Integer getFinalScorePlayerA() {
        return finalScorePlayerA;
    }

    public void setFinalScorePlayerA(final Integer finalScorePlayerA) {
        this.finalScorePlayerA = finalScorePlayerA;
    }

    public Integer getFinalScorePlayerB() {
        return finalScorePlayerB;
    }

    public void setFinalScorePlayerB(final Integer finalScorePlayerB) {
        this.finalScorePlayerB = finalScorePlayerB;
    }

    public Integer getPlayerAreportedScoreForPlayerA() {
        return playerAreportedScoreForPlayerA;
    }

    public void setPlayerAreportedScoreForPlayerA(final Integer playerAreportedScoreForPlayerA) {
        this.playerAreportedScoreForPlayerA = playerAreportedScoreForPlayerA;
    }

    public Integer getPlayerAreportedScoreForPlayerB() {
        return playerAreportedScoreForPlayerB;
    }

    public void setPlayerAreportedScoreForPlayerB(final Integer playerAreportedScoreForPlayerB) {
        this.playerAreportedScoreForPlayerB = playerAreportedScoreForPlayerB;
    }

    public Integer getPlayerBreportedScoreForPlayerA() {
        return playerBreportedScoreForPlayerA;
    }

    public void setPlayerBreportedScoreForPlayerA(final Integer playerBreportedScoreForPlayerA) {
        this.playerBreportedScoreForPlayerA = playerBreportedScoreForPlayerA;
    }

    public Integer getPlayerBreportedScoreForPlayerB() {
        return playerBreportedScoreForPlayerB;
    }

    public void setPlayerBreportedScoreForPlayerB(final Integer playerBreportedScoreForPlayerB) {
        this.playerBreportedScoreForPlayerB = playerBreportedScoreForPlayerB;
    }

    public Race getRacePlayerA() {
        return racePlayerA;
    }

    public void setRacePlayerA(final Race racePlayerA) {
        this.racePlayerA = racePlayerA;
    }

    public Race getRacePlayerB() {
        return racePlayerB;
    }

    public void setRacePlayerB(final Race racePlayerB) {
        this.racePlayerB = racePlayerB;
    }

    public void setGameStatus(final GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
