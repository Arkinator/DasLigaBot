package com.planed.ctlBot.domain;

import com.planed.ctlBot.common.GameStatus;
import com.planed.ctlBot.common.Race;

import java.util.List;

public class Match {
    private long matchId;
    private List<User> players;
    private GameStatus gameStatus = GameStatus.challengeExtended;
    private Integer finalScorePlayerA;
    private Integer finalScorePlayerB;
    private Race racePlayerA;
    private Race racePlayerB;
    private Integer playerAreportedScoreForPlayerA;
    private Integer playerAreportedScoreForPlayerB;
    private Integer playerBreportedScoreForPlayerA;
    private Integer playerBreportedScoreForPlayerB;

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(final long matchId) {
        this.matchId = matchId;
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

    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(final List<User> players) {
        this.players = players;
    }

    public void setGameStatus(final GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
