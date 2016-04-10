package com.planed.ctlBot.commands;

import com.planed.ctlBot.data.CtlMatch;
import org.mockito.ArgumentMatcher;

import java.util.Optional;

/**
 * Created by Julian Peters on 10.04.16.
 *
 * @author julian.peters@westernacher.com
 */
class MatchEqualityMatcher extends ArgumentMatcher<CtlMatch> {
    private final String name2;
    private final String name1;
    private final Optional<String> winnerOptional;

    public MatchEqualityMatcher(String name1, String name2) {
        this.name1 = name1;
        this.name2 = name2;
        this.winnerOptional = Optional.empty();
    }

    public MatchEqualityMatcher(String name1, String name2, String winner) {
        this.name1 = name1;
        this.name2 = name2;
        this.winnerOptional = Optional.of(winner);
    }

    @Override
    public boolean matches(Object o) {
        if (o instanceof CtlMatch
                && ((CtlMatch) o).getPlayer1().equals(name1)
                && ((CtlMatch) o).getPlayer2().equals(name2)) {
            if (winnerOptional.isPresent()){
                winnerOptional.get().equals(((CtlMatch) o).getWinner());
            } else {
                return true;
            }
        }
        return false;
    }
}
