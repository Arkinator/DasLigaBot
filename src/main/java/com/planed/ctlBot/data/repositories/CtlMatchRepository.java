package com.planed.ctlBot.data.repositories;

import com.planed.ctlBot.data.CtlMatch;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public interface CtlMatchRepository extends CrudRepository<CtlMatch, Long> {
    List<CtlMatch> findMatchByPlayer1(String player1);

    List<CtlMatch> findMatchByPlayer2(String player2);
}

