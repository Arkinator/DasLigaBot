package com.planed.ctlBot.services;

import com.planed.ctlBot.data.CtlMatch;
import com.planed.ctlBot.data.repositories.CtlMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Julian Peters on 10.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
public class CtlMatchService {
    private CtlMatchRepository ctlMatchRepository;

    @Autowired
    public CtlMatchService(CtlMatchRepository ctlMatchRepository) {
        this.ctlMatchRepository = ctlMatchRepository;
    }

    public List<CtlMatch> findAllMatchesInCurrentWeek() {
        List<CtlMatch> result = new ArrayList<>();
        ctlMatchRepository.findAll().forEach(result::add);
        return result;
    }

    public Optional<CtlMatch> findMatchInCurrentWeekForUser(String user) {
        for(CtlMatch ctlMatch : findAllMatchesInCurrentWeek()) {
            if(ctlMatch.getPlayer2().equals(user) || ctlMatch.getPlayer1().equals(user))
                return Optional.of(ctlMatch);
        }
        return Optional.empty();
    }

    public void saveMatch(CtlMatch match) {
        ctlMatchRepository.save(match);
    }

    public void deleteAllMatches() {
        ctlMatchRepository.findAll().forEach(ctlMatchRepository::delete);
    }
}
