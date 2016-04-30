package com.planed.ctlBot.data.repositories;

import com.planed.ctlBot.data.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends CrudRepository<UserEntity, String> {
}

