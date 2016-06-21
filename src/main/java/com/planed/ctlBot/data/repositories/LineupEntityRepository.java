package com.planed.ctlBot.data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.planed.ctlBot.data.LineupEntity;

/**
 * 
 * @author azthec
 *
 */
@Repository
public interface LineupEntityRepository extends CrudRepository<LineupEntity, String> { //this somehow maps objects to db entities MAGIC
}
