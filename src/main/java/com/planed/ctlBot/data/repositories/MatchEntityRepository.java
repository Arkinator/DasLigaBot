package com.planed.ctlBot.data.repositories;

import com.planed.ctlBot.data.MatchEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface MatchEntityRepository extends CrudRepository<MatchEntity, Long> {
    Collection<MatchEntity> findMatchByPlayerA(String discordId);

    Collection<MatchEntity> findMatchByPlayerB(String discordId);
}

