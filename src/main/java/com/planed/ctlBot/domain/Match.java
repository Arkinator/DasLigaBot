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
    private String originatingServerId;
    private String originatingChannelId;

    public boolean didUserReportResult(final User player) {
        if (isPlayerA(player) && playerAreportedScoreForPlayerA != null) {
            return true;
        }
        if (isPlayerB(player) && playerBreportedScoreForPlayerA != null) {
            return true;
        }
        return false;
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
                setGameStatus(GameStatus.gamePlayed);
                setFinalScorePlayerA(playerAreportedScoreForPlayerA);
                setFinalScorePlayerB(playerAreportedScoreForPlayerB);
            } else {
                setGameStatus(GameStatus.conflictState);
            }
        } else if ((playerBreportedScoreForPlayerA != null) || (playerAreportedScoreForPlayerA != null)) {
            setGameStatus(GameStatus.partiallyReported);
        } else {
            setGameStatus(GameStatus.challengeAccepted);
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
        if (gameStatus == GameStatus.challengeExtended) {
            vs = " (vs) ";
            status = " (extended challenge)";
        }
        if (gameStatus == GameStatus.challengeAccepted) {
            status = " (accepted challenge)";
        }
        if (gameStatus == GameStatus.partiallyReported) {
            status = " (partially reported)";
        }
        if (gameStatus == GameStatus.conflictState) {
            status = " (conflict state)";
        }
        if (gameStatus == GameStatus.gamePlayed) {
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
