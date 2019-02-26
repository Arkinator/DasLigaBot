package com.planed.ctlBot.common;

import java.util.Arrays;
import java.util.List;

public enum GameResult {
    win(1, 0, "win", "won"),
    draw(1, 1, "drawn", "equal"),
    loss(0, 1, "loss", "loose", "shit", "fuck", "bastard");

    private final List<String> alternativeName;
    private final int home;
    private final int away;

    GameResult(final int home, final int away, final String... alternativeName) {
        this.alternativeName = Arrays.asList(alternativeName);
        this.home = home;
        this.away = away;
    }

    public static GameResult parse(final String input) {
        for (final GameResult result : values()) {
            for (final String name : result.alternativeName) {
                if (name.equalsIgnoreCase(input)) {
                    return result;
                }
            }
        }
        return null;
    }

    public int getScoreForHome() {
        return home;
    }

    public int getScoreForAway() {
        return away;
    }
}
