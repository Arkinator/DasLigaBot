package com.planed.ctlBot.domain;

import com.planed.ctlBot.data.UserEntity;
import com.planed.ctlBot.data.repositories.UserEntityRepository;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.StreamUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepository {
    private final Mapper mapper = new DozerBeanMapper();
    private final UserEntityRepository userEntityRepository;

    @Autowired
    public UserRepository(final UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    public List<User> findAll() {
        final List<User> result = new ArrayList<>();
        StreamUtils.createStreamFromIterator(userEntityRepository.findAll().iterator())
                .map(o->mapper.map(o, User.class))
                .forEach(result::add);
        return result;
    }

    public User findByDiscordId(final String discordId) {
        final UserEntity userEntity = userEntityRepository.findOne(discordId);
        if (userEntity == null)
            return null;
        return mapper.map(userEntity, User.class);
    }

    public void save(final User... users) {
        for (final User user : users) {
            final UserEntity userEntity = mapper.map(user, UserEntity.class);
            userEntityRepository.save(userEntity);
        }
    }

    public User refresh(final User author) {
        return mapper.map(userEntityRepository.findOne(author.getDiscordId()), User.class);
    }
}
