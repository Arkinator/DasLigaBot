package com.planed.ctlBot.data.repositories;

import com.planed.ctlBot.data.MatchEntity;
import com.planed.ctlBot.data.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public interface MatchEntityRepository extends CrudRepository<MatchEntity, Long> {
    List<MatchEntity> findMatchByPlayers(UserEntity player);
}

