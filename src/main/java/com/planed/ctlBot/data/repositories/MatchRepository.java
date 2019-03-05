package com.planed.ctlBot.data.repositories;

import com.planed.ctlBot.domain.Match;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MatchRepository extends CrudRepository<Match, Long> {
    Optional<Match> findMatchByPlayerA(String discordId);

    Optional<Match> findMatchByPlayerB(String discordId);
}

