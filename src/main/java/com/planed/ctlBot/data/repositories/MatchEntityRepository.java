package com.planed.ctlBot.data.repositories;

import com.planed.ctlBot.data.MatchEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public interface MatchEntityRepository extends CrudRepository<MatchEntity, Long> {
    Collection<? extends MatchEntity> findMatchByPlayerA(String discordId);
    Collection<? extends MatchEntity> findMatchByPlayerB(String discordId);
}

