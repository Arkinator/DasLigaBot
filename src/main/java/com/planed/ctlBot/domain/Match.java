package com.planed.ctlBot.domain;

import com.planed.ctlBot.common.GameResult;
import com.planed.ctlBot.common.GameStatus;
import com.planed.ctlBot.common.Race;
import lombok.Data;

import java.util.List;

@Data
public class Match {
    private long matchId;
    private List<User> players;
    private GameStatus gameStatus;
    private Integer finalScorePlayerA;
    private Integer finalScorePlayerB;
    private Race racePlayerA;
    private Race racePlayerB;
    private Integer playerAreportedScoreForPlayerA;
    private Integer playerAreportedScoreForPlayerB;
    private Integer playerBreportedScoreForPlayerA;
    private Integer playerBreportedScoreForPlayerB;
    private Long originatingServerId;
    private Long originatingChannelId;

    public boolean didUserReportResult(final User player) {
        if (isPlayerA(player) && playerAreportedScoreForPlayerA != null) {
            return true;
        }
        return isPlayerB(player) && playerBreportedScoreForPlayerA != null;
    }

    public void reportResult(final User reporter, final GameResult result) {
        if (isPlayerA(reporter)) {
            playerAreportedScoreForPlayerA = result.getScoreForHome();
            playerAreportedScoreForPlayerB = result.getScoreForAway();
            updateGameStatus();
        } else if (isPlayerB(reporter)) {
            playerBreportedScoreForPlayerA = result.getScoreForAway();
            playerBreportedScoreForPlayerB = result.getScoreForHome();
            updateGameStatus();
        }
    }

    private void updateGameStatus() {
        if ((playerBreportedScoreForPlayerA != null) && (playerAreportedScoreForPlayerA != null)) {
            if (reportsAreMatching()) {
                setGameStatus(GameStatus.GAME_PLAYED);
                setFinalScorePlayerA(playerAreportedScoreForPlayerA);
                setFinalScorePlayerB(playerAreportedScoreForPlayerB);
            } else {
                setGameStatus(GameStatus.CONFLICT_STATE);
            }
        } else if ((playerBreportedScoreForPlayerA != null) || (playerAreportedScoreForPlayerA != null)) {
            setGameStatus(GameStatus.PARTIALLY_REPORTED);
        } else {
            setGameStatus(GameStatus.CHALLENGE_ACCEPTED);
        }
    }

    private boolean isPlayerA(final User player) {
        return player.getDiscordId().equals(players.get(0).getDiscordId());
    }

    private boolean isPlayerB(final User player) {
        return player.getDiscordId().equals(players.get(1).getDiscordId());
    }

    public boolean reportsAreMatching() {
        return (playerAreportedScoreForPlayerA == playerBreportedScoreForPlayerA) &&
                (playerAreportedScoreForPlayerB == playerBreportedScoreForPlayerB);
    }

    @Override
    public String toString() {
        String scoreA = "";
        String scoreB = "";
        String vs = " vs ";
        String status = "";
        if (gameStatus == GameStatus.CHALLENGE_EXTENDED) {
            vs = " (vs) ";
            status = " (extended challenge)";
        }
        if (gameStatus == GameStatus.CHALLENGE_ACCEPTED) {
            status = " (accepted challenge)";
        }
        if (gameStatus == GameStatus.PARTIALLY_REPORTED) {
            status = " (partially reported)";
        }
        if (gameStatus == GameStatus.CONFLICT_STATE) {
            status = " (conflict state)";
        }
        if (gameStatus == GameStatus.GAME_PLAYED) {
            scoreA = "(" + finalScorePlayerA + ")";
            scoreB = "(" + finalScorePlayerB + ")";
        }
        String playerA = "<>";
        String playerB = "<>";
        if (players.size() == 2) {
            playerA = players.get(0).toString();
            playerB = players.get(1).toString();
        }
        return playerA + scoreA + vs + scoreB + playerB + status;
    }

    public double getEloResult() {
        return ((finalScorePlayerA - finalScorePlayerB) + 1) / 2;
    }

    public User getPlayerA() {
        return players.get(0);
    }

    public User getPlayerB() {
        return players.get(1);
    }
}
