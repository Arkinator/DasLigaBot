package com.planed.ctlBot.data.repositories;

import com.planed.ctlBot.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByLoginAuthorizationCode(String loginAuthorizationCode);

    Optional<User> findByDiscordId(String discordId);
}

