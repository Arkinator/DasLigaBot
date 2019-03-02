package com.planed.ctlBot.data.repositories;

import com.planed.ctlBot.data.DiscordServer;
import org.springframework.data.repository.CrudRepository;

public interface ServerRepository extends CrudRepository<DiscordServer, Long> {
}
