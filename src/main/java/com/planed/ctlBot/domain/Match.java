package com.planed.ctlBot.domain;

import com.planed.ctlBot.common.GameResult;
import com.planed.ctlBot.common.GameStatus;
import com.planed.ctlBot.common.Race;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "MATCHES")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long matchId;

    @Column
    private String playerA;
    @Column
    private String playerB;
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

    @Column
    private Long originatingServerId;
    @Column
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
        return player.getDiscordId().equals(playerA);
    }

    private boolean isPlayerB(final User player) {
        return player.getDiscordId().equals(playerB);
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
        return playerA + scoreA + vs + scoreB + playerB + status;
    }

    public double getEloResult() {
        return ((finalScorePlayerA - finalScorePlayerB) + 1) / 2;
    }
}
