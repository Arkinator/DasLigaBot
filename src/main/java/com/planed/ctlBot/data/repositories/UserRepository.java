package com.planed.ctlBot.data.repositories;

import com.planed.ctlBot.data.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findByDiscordId(String userDiscordId);
}

